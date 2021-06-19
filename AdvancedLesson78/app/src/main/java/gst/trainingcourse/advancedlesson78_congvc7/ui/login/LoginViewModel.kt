package gst.trainingcourse.advancedlesson78_congvc7.ui.login

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import gst.trainingcourse.advancedlesson78_congvc7.R
import gst.trainingcourse.advancedlesson78_congvc7.model.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val email = MutableLiveData("")
    val password = MutableLiveData("")

    val emailError: LiveData<Int> = email.map(User.Companion::mapEmailError)

    val passwordError: LiveData<Int> = password.map(User.Companion::mapPasswordError)

    val loginButtonEnabled: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(email) {
            value = User.validateEmail(it) && User.validatePassword(password.value!!)
        }
        addSource(password) {
            value = User.validatePassword(it) && User.validateEmail(email.value!!)
        }
    }

    private val _navigateRequest = MutableSharedFlow<NavDirections>()
    val navigateRequest: SharedFlow<NavDirections> get() = _navigateRequest

    private val _errorToast = MutableSharedFlow<@StringRes Int>()
    val errorToast: SharedFlow<Int> get() = _errorToast

    private val _hideSoftKeyboard = MutableSharedFlow<@StringRes Unit>()
    val hideSoftKeyboard: SharedFlow<Unit> get() = _hideSoftKeyboard

    private var cachedLoginJob: Job? = null

    @MainThread
    fun login() {
        cachedLoginJob?.cancel()
        cachedLoginJob = viewModelScope.launch {
            _hideSoftKeyboard.emit(Unit)
            val em = email.value!!
            val pw = password.value!!
            val user = User(em, pw)
            when {
                !User.validateEmail(em) || !User.validatePassword(pw) -> {
                }
                User.login(user) -> {
                    _navigateRequest.emit(LoginFragmentDirections.actionLoginToHome(em))
                }
                else -> {
                    _errorToast.emit(R.string.wrong_email_password)
                }
            }
        }
    }

    fun navigateToSignup() {
        viewModelScope.launch {
            _navigateRequest.emit(LoginFragmentDirections.actionLoginToSignup())
        }
    }

    @MainThread
    fun clearAllInput() {
        email.value = ""
        password.value = ""
    }
}
