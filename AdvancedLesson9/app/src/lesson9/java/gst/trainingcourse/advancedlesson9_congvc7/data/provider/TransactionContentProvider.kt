package gst.trainingcourse.advancedlesson9_congvc7.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.sqlite.db.SupportSQLiteQueryBuilder
import gst.trainingcourse.advancedlesson9_congvc7.data.database.AppDatabase
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import gst.trainingcourse.advancedlesson9_congvc7.di.Injector
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.AUTHORITY
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.Columns._ID
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.PATH
import gst.trainingcourse.advancedlesson9_congvc7.util.Transaction
import kotlinx.coroutines.runBlocking

private const val MATCH_TRANSACTION_TABLE = 1
private const val MATCH_SINGLE_TRANSACTION_ROW = 2

class TransactionContentProvider : ContentProvider() {
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, PATH, MATCH_TRANSACTION_TABLE)
        addURI(AUTHORITY, "$PATH/#", MATCH_SINGLE_TRANSACTION_ROW)
    }

    private lateinit var database: AppDatabase
    private val transactionDao by lazy {
        database.transactionDao
    }

    /**
     * FACT: ContentProvider#onCreate is ALWAYS called before it's Application#onCreate be called.
     * See "TAG" tag in appA logcat
     */
    override fun onCreate(): Boolean {
        Log.d("TAG", "onCreate: ${context!!::class.java.name}")
        // NOTE: this will result in 2 instance of app database
        return try {
            database = Injector.getAppDatabase(context!!)
            true
        } catch (e: NullPointerException) {
            false
        }
    }

    @Suppress("RedundantNullableReturnType")
    @Throws(IllegalStateException::class)
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var localSortOrder: String = sortOrder ?: ""
        var localSelection: String = selection ?: ""
        when (uriMatcher.match(uri)) {
            MATCH_TRANSACTION_TABLE -> {
                if (sortOrder.isNullOrEmpty()) {
                    localSortOrder += "$_ID ASC"
                }
            }
            MATCH_SINGLE_TRANSACTION_ROW -> {
                localSelection += " ${if (localSelection.isNotBlank()) "AND" else ""} $_ID = ${uri.lastPathSegment}"
            }
            else -> {
                throwUnknownUri(uri)
            }
        }
        val query = SupportSQLiteQueryBuilder.builder(Transaction.TABLE_NAME)
            .columns(projection)
            .selection(localSelection, selectionArgs)
            .orderBy(localSortOrder)
            .create()
        return database.query(query)
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        require(uriMatcher.match(uri) == MATCH_TRANSACTION_TABLE) { "App A: Unknown URI: $uri" }

        if (values == null) return null

        return runBlocking {
            val ids = transactionDao.add(Transaction(values, Injector.converters))
            ids.firstOrNull()?.let {
                ContentUris.withAppendedId(TransactionContract.CONTENT_URI, it).also { uri ->
                    context?.contentResolver?.notifyChange(uri, null)
                }
            }
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return runBlocking {
            when (uriMatcher.match(uri)) {
                MATCH_TRANSACTION_TABLE -> {
                    if (selection == null) {
                        transactionDao.clearTable()
                    } else {
                        val query = SupportSQLiteQueryBuilder.builder(Transaction.TABLE_NAME)
                            .selection(selection, selectionArgs)
                            .create()
                        val ids = getAllTransactionInVoids(database.query(query)).toIntArray()
                        transactionDao.delete(*ids)
                    }
                }
                MATCH_SINGLE_TRANSACTION_ROW -> {
                    val id = ContentUris.parseId(uri)
                    transactionDao.delete(id.toInt())
                }
                else -> {
                    throwUnknownUri(uri)
                }
            }
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw UnsupportedOperationException()
    }

    private fun getAllTransactionInVoids(cursor: Cursor): List<Int> {
        check(!cursor.isClosed) {}
        if (cursor.count == 0) return emptyList()

        return cursor.use {
            val result = mutableListOf<Int>()
            while (it.moveToNext()) {
                result += it.getInt(it.getColumnIndex(Transaction.TABLE_NAME))
            }
            result
        }
    }

    private fun throwUnknownUri(uri: Uri): Nothing =
        throw IllegalStateException("App A: Unknown URI: $uri")
}
