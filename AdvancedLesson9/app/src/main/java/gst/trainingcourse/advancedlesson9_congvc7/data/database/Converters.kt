package gst.trainingcourse.advancedlesson9_congvc7.data.database

import androidx.room.TypeConverter
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import java.util.Date

class Converters {
    @TypeConverter
    fun toDate(value: Long?): Date? {
        return value?.let(::Date)
    }

    @TypeConverter
    fun fromDate(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun fromTransactionType(value: Transaction.Type?): Byte? {
        return value?.ordinal?.toByte()
    }

    @TypeConverter
    fun toTransactionType(value: Byte?): Transaction.Type? {
        return value?.toInt()?.let(Transaction.Type.values()::get)
    }

    @TypeConverter
    fun fromCurrency(value: Currency?): Byte? {
        return value?.ordinal?.toByte()
    }

    @TypeConverter
    fun toCurrency(value: Byte?): Currency? {
        return value?.toInt()?.let(Currency.values()::get)
    }
}
