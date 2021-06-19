package gst.trainingcourse.advancedlesson9_congvc7.data.repo

import gst.trainingcourse.advancedlesson9_congvc7.data.database.TransactionDao
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import gst.trainingcourse.advancedlesson9_congvc7.ui.add.AddTransactionRepo
import gst.trainingcourse.advancedlesson9_congvc7.ui.home.HomeTransactionRepo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.coroutines.CoroutineContext

class TransactionRepoImpl(
    private val transactionDao: TransactionDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : TransactionRepo.Insertable, TransactionRepo.Queryable, TransactionRepo.Deletable,
    HomeTransactionRepo, AddTransactionRepo,
    CoroutineScope {

    override val coroutineContext: CoroutineContext get() = SupervisorJob() + dispatcher

    private val _lazilyRemovedTransactionInVoids = MutableStateFlow<IntArray?>(null)

    override val isLazilyRemovalDismissible: StateFlow<Boolean> = _lazilyRemovedTransactionInVoids
        .map { it != null }
        .stateIn(
            scope = this,
            started = SharingStarted.Lazily,
            initialValue = false,
        )

    override suspend fun add(vararg transaction: Transaction): LongArray {
        return transactionDao.add(*transaction)
    }

    override fun getAll(): Flow<List<Transaction>> {
        return transactionDao.getAll().filterLazilyRemovedItem()
    }

    override suspend fun find(
        transactionType: Transaction.Type?,
        period: Pair<Date, Date>?,
        holderName: String?,
        currency: Currency?
    ): Flow<List<Transaction>> {
        return transactionDao.find(transactionType, period, holderName, currency)
            .filterLazilyRemovedItem()
    }

    private var _removeJob: Job? = null
    override suspend fun removeLazily(vararg inVoid: Int, delayMillis: Long) {
        // new coroutineScope every call, suspend until Transactions are deleted from table
        coroutineScope {
            _removeJob?.cancel(RemoveTransactionCancellationException.NewRemoveRequest())
            _removeJob = launch(dispatcher) {
                try {
                    _lazilyRemovedTransactionInVoids.value = inVoid
                    delay(delayMillis)
                    transactionDao.delete(*inVoid)
                } catch (e: RemoveTransactionCancellationException) {
                    /* if e is of type CancellationException but not
                    * RemoveTransactionCancellationException meaning that cancellation event comes
                    * from outer coroutineScope and isn't considered a user-expected dismission
                    * intention */
                    when (e) {
                        is RemoveTransactionCancellationException.Dismiss -> {
                            // user's intention. Do nothing
                        }
                        is RemoveTransactionCancellationException.NewRemoveRequest -> {
                            // start a new coroutine and forget
                            this@coroutineScope.launch(dispatcher) {
                                transactionDao.delete(*inVoid)
                            }
                        }
                    }
                } finally {
                    withContext(NonCancellable) {
                        _lazilyRemovedTransactionInVoids.value = null
                    }
                }
            }
        }
    }

    override suspend fun dismissLazyRemoval() {
        _removeJob?.cancel(RemoveTransactionCancellationException.Dismiss())
    }

    override suspend fun clearAllTransaction(): Int {
        return transactionDao.clearTable()
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun Flow<List<Transaction>>.filterLazilyRemovedItem() =
        this.combine(_lazilyRemovedTransactionInVoids) { list, i ->
            if (i == null) return@combine list
            list.filterNot { it.inVoidNumber in i }
        }

    private sealed class RemoveTransactionCancellationException : CancellationException() {
        class Dismiss : RemoveTransactionCancellationException()
        class NewRemoveRequest : RemoveTransactionCancellationException()
    }
}
