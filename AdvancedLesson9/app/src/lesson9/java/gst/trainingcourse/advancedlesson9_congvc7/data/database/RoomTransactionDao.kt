package gst.trainingcourse.advancedlesson9_congvc7.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
abstract class RoomTransactionDao : TransactionDao {

    /**
     * @return indexes array of all successfully added [Transaction]
     */
    @Insert
    abstract override suspend fun add(vararg transaction: Transaction): LongArray

    @Query("SELECT * FROM transaction_tb")
    abstract override fun getAll(): Flow<List<Transaction>>

    override fun find(
        transactionType: Transaction.Type?,
        period: Pair<Date, Date>?,
        holderName: String?,
        currency: Currency?,
    ) = find(transactionType, period?.first, period?.second, holderName, currency)

    @Query(
        """
        SELECT * FROM transaction_tb 
        WHERE (:transactionType IS NULL OR type = :transactionType) AND (
                (:startDate IS NULL AND :endDate IS NULL) OR 
                (:startDate IS NOT NULL AND :endDate IS NULL AND dateTime >= :startDate) OR 
                (:startDate IS NULL AND :endDate IS NOT NULL AND dateTime <= :endDate) OR 
                (dateTime BETWEEN :startDate AND :endDate)
            ) AND 
            (:holderName IS NULL OR holderName LIKE :holderName) AND 
            (:currency IS NULL OR currency = :currency)"""
    )
    protected abstract fun find(
        transactionType: Transaction.Type?,
        startDate: Date?,
        endDate: Date?,
        holderName: String?,
        currency: Currency?,
    ): Flow<List<Transaction>>

    @Query("""DELETE FROM transaction_tb WHERE inVoidNumber IN (:inVoid)""")
    abstract override suspend fun delete(vararg inVoid: Int): Int

    /**
     * @return number of [Transaction] rows deleted
     */
    @Query("DELETE FROM transaction_tb")
    abstract override suspend fun clearTable(): Int
}
