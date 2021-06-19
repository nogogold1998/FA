package gst.trainingcourse.advancedlesson9_congvc7.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val UNDO_DELAY = 3000L

class HomeViewModel(private val transactionRepo: HomeTransactionRepo) : ViewModel() {
    class Factory(private val transactionRepo: HomeTransactionRepo) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(transactionRepo) as T
        }
    }

    val currency = MutableLiveData(Currency.USD)

     val allTransactions = transactionRepo
        .getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList(),
        )

    val transactions: LiveData<List<Transaction>> = allTransactions.asLiveData()

    val revenue = currency.asFlow().combine(allTransactions) { currency, transactions ->
        transactions.fold(0f) { sum, transaction ->
            sum + transaction.getRevenue(currency)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = 0f,
    ).asLiveData()

    val isLazilyRemovalDismissible = transactionRepo.isLazilyRemovalDismissible

    fun removeTransaction(transactionInVoid: Int) {
        viewModelScope.launch {
            transactionRepo.removeLazily(transactionInVoid, delayMillis = UNDO_DELAY)
        }
    }

    fun deleteAllTransactions() {
        viewModelScope.launch {
            transactionRepo.clearAllTransaction()
        }
    }

    fun undoRemoval() {
        viewModelScope.launch {
            transactionRepo.dismissLazyRemoval()
        }
    }
}
