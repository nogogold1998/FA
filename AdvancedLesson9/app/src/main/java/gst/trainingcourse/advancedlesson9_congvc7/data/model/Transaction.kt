package gst.trainingcourse.advancedlesson9_congvc7.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction.Companion.TABLE_NAME
import java.util.Date

@Entity(tableName = TABLE_NAME)
data class Transaction(
    val type: Type,
    val amount: Float,
    val currency: Currency,
    val holderName: String,
    val dateTime: Date,
    @PrimaryKey(autoGenerate = true)
    val inVoidNumber: Int = 0,
) {

    @Suppress("NOTHING_TO_INLINE")
    private inline fun getBalance(targetCur: Currency): Float = if (targetCur == currency) {
        amount
    } else {
        amount / currency.transferRate * targetCur.transferRate
    }

    fun getRevenue(targetCur: Currency): Float = getBalance(targetCur) * type.revenueRate

    enum class Type(val revenueRate: Float) {
        SALE(1f),
        REFUND(-1f);

        companion object
    }

    companion object {
        const val TABLE_NAME = "transaction_tb"
    }
}
