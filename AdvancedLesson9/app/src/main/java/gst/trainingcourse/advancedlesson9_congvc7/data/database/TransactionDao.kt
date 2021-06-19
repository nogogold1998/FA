package gst.trainingcourse.advancedlesson9_congvc7.data.database

import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionDao {
    suspend fun add(vararg transaction: Transaction): LongArray

    fun getAll(): Flow<List<Transaction>>

    fun find(
        transactionType: Transaction.Type? = null,
        period: Pair<Date, Date>? = null,
        holderName: String? = null,
        currency: Currency? = null,
    ): Flow<List<Transaction>>

    suspend fun delete(vararg inVoid: Int): Int

    suspend fun clearTable(): Int
}
