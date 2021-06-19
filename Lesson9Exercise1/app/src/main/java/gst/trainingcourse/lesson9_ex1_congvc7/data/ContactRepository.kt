package gst.trainingcourse.lesson9_ex1_congvc7.data

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import androidx.annotation.AnyThread
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

interface ContactRepository {
    @AnyThread
    fun getAll(contentResolver: ContentResolver): Flow<List<Contact>>
}

private val CONTACT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

private val PROJECTION = arrayOf(
    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,  // 0
    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,// 1
    ContactsContract.CommonDataKinds.Phone.NUMBER,      // 2
)

private const val CONTACT_ID_INDEX = 0
private const val CONTACT_DISPLAY_NAME_INDEX = 1
private const val CONTACT_PHONE_NUMBER_INDEX = 2

private val SELECTION = null //"${ContactsContract.Contacts.HAS_PHONE_NUMBER} = ?"
private val ARGUMENTS = null //arrayOf("1")

class ContactRepositoryImpl : ContactRepository {

    override fun getAll(contentResolver: ContentResolver): Flow<List<Contact>> {
        val result = contentResolver.query(
            CONTACT_URI,
            PROJECTION,
            SELECTION,
            ARGUMENTS,
            null,
        )
        if (result?.moveToFirst() == false) result.close()
        if (result == null || result.isClosed) return emptyFlow()
        return flow {
            try {
                val contacts = mutableListOf<Contact>()
                do {
                    contacts += extractContactFromRow(result)
                    delay(100L)
                    emit(contacts.toList())
                } while (result.moveToNext())
            } finally {
                withContext(NonCancellable) {
                    result.close()
                }
            }
        }
    }

    private fun extractContactFromRow(cursor: Cursor): Contact {
        return Contact(
            cursor.getLong(CONTACT_ID_INDEX),
            cursor.getString(CONTACT_DISPLAY_NAME_INDEX),
            cursor.getStringOrNull(CONTACT_PHONE_NUMBER_INDEX).toString(),
        )
    }
}
