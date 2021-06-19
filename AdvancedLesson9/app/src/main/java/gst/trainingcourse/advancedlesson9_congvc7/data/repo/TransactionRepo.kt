package gst.trainingcourse.advancedlesson9_congvc7.data.repo

import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date

// namespace
object TransactionRepo {
    interface Insertable {
        suspend fun add(vararg transaction: Transaction): LongArray
    }

    interface Queryable {
        fun getAll(): Flow<List<Transaction>>

        suspend fun find(
            transactionType: Transaction.Type? = null,
            period: Pair<Date, Date>? = null,
            holderName: String? = null,
            currency: Currency? = null,
        ): Flow<List<Transaction>>
    }

    interface Deletable {
        val isLazilyRemovalDismissible: StateFlow<Boolean>

        /**
         * lazily remove [Transaction] from all exposed [TransactionRepo] methods' results but
         * wait for [delayMillis] millSeconds before actually delete [Transaction]s, having
         * [Transaction.inVoidNumber] matches [inVoid] values after suspend, from the database.
         *
         * if there is a second call [removeLazily], it will skip the interval and call deleting [Transaction]s
         * from the previous invocation immediately. This ensures that [Transaction]s will be
         * completely removed.
         *
         * if you want to dismiss ongoing remove request, call [dismissLazyRemoval]
         *
         * @param delayMillis interval before the actual call deleting [Transaction] in database
         * @param inVoid id(s) of [Transaction] you request to remove
         * @throws [kotlinx.coroutines.CancellationException]
         */
        suspend fun removeLazily(vararg inVoid: Int, delayMillis: Long = 0L)

        /**
         * dismiss previous call to [removeLazily] if there's still a lazily [removeLazily] job waiting to actually
         * delete [Transaction]s from the database.
         */
        suspend fun dismissLazyRemoval()

        suspend fun clearAllTransaction(): Int
    }
}
