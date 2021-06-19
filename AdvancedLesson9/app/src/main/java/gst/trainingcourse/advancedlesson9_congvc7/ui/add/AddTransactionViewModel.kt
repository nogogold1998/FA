package gst.trainingcourse.advancedlesson9_congvc7.ui.add

import android.icu.util.Calendar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.advancedlesson9_congvc7.R
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Currency
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AddTransactionViewModel(private val transactionRepo: AddTransactionRepo) : ViewModel() {
    class Factory(
        private val transactionRepo: AddTransactionRepo
    ) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return AddTransactionViewModel(transactionRepo) as T
        }
    }

    val transactionType = MutableLiveData(Transaction.Type.SALE)

    val holderName = MutableLiveData("")

    val errorHolderName: LiveData<Int> = holderName.map {
        if (it.isNullOrBlank()) {
            R.string.error_name_short
        } else {
            Int.MIN_VALUE
        }
    }

    private val _amount = MutableStateFlow(0f)
    val amount: LiveData<String> get() = _amount.map { it.toString() }.asLiveData()

    val currency = MutableLiveData(Currency.USD)

    fun changeAmount(amount: String?) {
        if (amount.isNullOrEmpty()) {
            _amount.value = 0f
            return
        }
        _amount.value = amount.toFloatOrNull() ?: 0f
    }

    private val _navigateToHomeRequest = MutableSharedFlow<Unit>()
    val navigateToHomeRequest: SharedFlow<Unit> get() = _navigateToHomeRequest

    fun addTransaction() {
        viewModelScope.launch {
            transactionRepo.add(extractTransaction())
            _navigateToHomeRequest.emit(Unit)
        }
    }

    private fun extractTransaction() = Transaction(
        transactionType.value!!,
        _amount.value,
        currency.value!!,
        holderName.value!!,
        Calendar.getInstance().time,
        0
    )
}
