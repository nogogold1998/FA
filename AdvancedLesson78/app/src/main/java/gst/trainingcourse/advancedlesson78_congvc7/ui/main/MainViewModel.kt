package gst.trainingcourse.advancedlesson78_congvc7.ui.main

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?>
        get() = _email

    private val _hideSoftKeyboardRequest = MutableSharedFlow<@StringRes Unit>()
    val hideSoftKeyboardRequest: SharedFlow<Unit> get() = _hideSoftKeyboardRequest

    fun loadUser(email: String) {
        _email.value = email
    }

    fun hideSoftKeyboard() {
        viewModelScope.launch {
            _hideSoftKeyboardRequest.emit(Unit)
        }
    }
}
