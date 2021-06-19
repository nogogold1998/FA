package gst.trainingcourse.advancedlesson9_congvc7.data.database

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import androidx.core.content.ContentResolverCompat
import androidx.core.os.CancellationSignal
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.Columns._ID
import gst.trainingcourse.advancedlesson9_congvc7.util.Transaction
import gst.trainingcourse.advancedlesson9_congvc7.util.toContentValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.Date

@ExperimentalCoroutinesApi
class TransactionDaoImpl(
    private val contentResolver: ContentResolver,
    private val converters: Converters,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TransactionDao {
    override suspend fun add(vararg transaction: Transaction): LongArray = coroutineScope {
        val deferred = transaction.map {
            async(dispatcher) {
                val insertedUri = contentResolver.insert(
                    TransactionContract.CONTENT_URI,
                    it.toContentValue(converters)
                )

                insertedUri?.let(ContentUris::parseId) ?: -1
            }
        }
        deferred.awaitAll().toLongArray()
    }

    override fun getAll(): Flow<List<Transaction>> = flow {
        val cancellationSignal = CancellationSignal()
        try {
            while (true) {
                val idsCursor = ContentResolverCompat.query(
                    contentResolver,
                    TransactionContract.CONTENT_URI,
                    arrayOf(_ID),
                    null, null, null,
                    cancellationSignal,
                )
                idsCursor?.use {
                    emit(extractTransactions(it))
                }

                delay(1000L)
            }
        } finally {
            cancellationSignal.cancel()
        }
    }

    private fun queryAllTransactionInVoids(cancellationSignal: CancellationSignal) =
        ContentResolverCompat.query(
            contentResolver,
            TransactionContract.CONTENT_URI,
            arrayOf(_ID),
            null, null, null,
            cancellationSignal,
        )

    private suspend fun extractTransactions(cursor: Cursor): List<Transaction> = coroutineScope {
        if (cursor.count == 0) return@coroutineScope emptyList()
        val deferred = mutableListOf<Deferred<Transaction>>()

        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex(_ID))
            val uri = ContentUris.withAppendedId(TransactionContract.CONTENT_URI, id)
            deferred += async(dispatcher) {
                val transactionCursor = contentResolver.query(
                    uri,
                    null,
                    null,
                    null,
                    null
                )!!
                transactionCursor.use {
                    it.moveToFirst()
                    Transaction(it, converters)
                }
            }
        }

        deferred.awaitAll()
    }

    override fun find(
        transactionType: Transaction.Type?,
        period: Pair<Date, Date>?,
        holderName: String?,
        currency: Currency?
    ): Flow<List<Transaction>> {
        throw UnsupportedOperationException()
    }

    override suspend fun delete(vararg inVoid: Int): Int = withContext(dispatcher) {
        contentResolver.delete(
            TransactionContract.CONTENT_URI, "$_ID in (?)",
            arrayOf(inVoid.joinToString())
        )
    }

    override suspend fun clearTable(): Int = withContext(dispatcher) {
        contentResolver.delete(
            TransactionContract.CONTENT_URI,
            null,
            null
        )
    }
}
