package gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract

import android.net.Uri

object TransactionContract {
    const val AUTHORITY = "gst.trainingcourse.advancedlesson9_congvc7"
    const val PATH = "transactions"

    val CONTENT_URI: Uri = Uri.Builder()
        .scheme("content")
        .authority(AUTHORITY)
        .appendPath(PATH)
        .build()

    object Columns {
        @Suppress("ObjectPropertyName")
        const val _ID = "inVoidNumber"
        const val TYPE = "type"
        const val AMOUNT = "amount"
        const val CURRENCY = "currency"
        const val HOLDER_NAME = "holderName"
        const val DATE_TIME = "dateTime"
    }

    object Permission {
        const val READ = "$AUTHORITY.$PATH.permission.READ"
        const val WRITE = "$AUTHORITY.$PATH.permission.WRITE"
    }
}


