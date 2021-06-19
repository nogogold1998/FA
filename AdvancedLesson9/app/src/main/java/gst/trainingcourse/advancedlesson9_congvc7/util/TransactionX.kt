package gst.trainingcourse.advancedlesson9_congvc7.util

import android.content.ContentValues
import android.database.Cursor
import gst.trainingcourse.advancedlesson9_congvc7.data.database.Converters
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.Columns.AMOUNT
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.Columns.CURRENCY
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.Columns.DATE_TIME
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.Columns.HOLDER_NAME
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.Columns.TYPE
import gst.trainingcourse.advancedlesson9_congvc7.lesson9.provider.contract.TransactionContract.Columns._ID

fun Transaction.toContentValue(converters: Converters): ContentValues {
    return ContentValues().apply {
        put(_ID, inVoidNumber)
        put(TYPE, converters.fromTransactionType(type))
        put(AMOUNT, amount)
        put(CURRENCY, converters.fromCurrency(currency))
        put(HOLDER_NAME, holderName)
        put(DATE_TIME, converters.fromDate(dateTime))
    }
}

fun Transaction(cursor: Cursor, converters: Converters): Transaction = with(cursor) {
    return Transaction(
        type = converters.toTransactionType(getShort(getColumnIndex(TYPE)).toByte())!!,
        amount = getFloat(getColumnIndex(AMOUNT)),
        currency = converters.toCurrency(getShort(getColumnIndex(CURRENCY)).toByte())!!,
        holderName = getString(getColumnIndex(HOLDER_NAME)),
        dateTime = converters.toDate(getLong(getColumnIndex(DATE_TIME)))!!,
        inVoidNumber = getInt(getColumnIndex(_ID)),
    )
}

fun Transaction(contentValues: ContentValues, converters: Converters): Transaction =
    with(contentValues) {
        return Transaction(
            type = converters.toTransactionType(getAsByte(TYPE))!!,
            amount = getAsFloat(AMOUNT),
            currency = converters.toCurrency(getAsByte(CURRENCY))!!,
            holderName = getAsString(HOLDER_NAME),
            dateTime = converters.toDate(getAsLong(DATE_TIME))!!,
            inVoidNumber = getAsInteger(_ID),
        )
    }
