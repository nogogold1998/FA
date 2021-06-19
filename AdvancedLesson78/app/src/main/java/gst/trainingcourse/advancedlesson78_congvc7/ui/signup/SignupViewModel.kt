package gst.trainingcourse.advancedlesson78_congvc7.ui.signup

import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import gst.trainingcourse.advancedlesson78_congvc7.R
import gst.trainingcourse.advancedlesson78_congvc7.model.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val repeatPassword = MutableLiveData("")

    val emailError = email.map(User.Companion::mapEmailError)
    val passwordError = password.map(User.Companion::mapPasswordError)
    val repeatPasswordError = password.asFlow().combine(repeatPassword.asFlow()) { _, rpw ->
        val errId = User.mapPasswordError(rpw)
        when {
            rpw.isEmpty() -> Int.MIN_VALUE
            errId != Int.MIN_VALUE -> errId
            rpw != password.value -> R.string.repeat_password_not_match
            else -> Int.MIN_VALUE
        }
    }.asLiveData()

    val signupButtonEnabled: LiveData<Boolean> = combine(
        email.asFlow(),
        password.asFlow(),
        repeatPassword.asFlow()
    ) { t1, t2, t3 ->
        User.validateEmail(t1) && User.validatePassword(t2) && t2 == t3
    }.asLiveData()

    private val _navigationRequest = MutableSharedFlow<NavDirections>()
    val navigationRequest: SharedFlow<NavDirections> get() = _navigationRequest

    private val _hideSoftKeyboardRequest = MutableSharedFlow<Unit>()
    val hideSoftKeyboardRequest: SharedFlow<Unit> get() = _hideSoftKeyboardRequest

    private val _errorToast = MutableSharedFlow<@StringRes Int>()
    val errorToast: SharedFlow<Int> get() = _errorToast

    private var cachedSignupJob: Job? = null

    @MainThread
    fun signup() {
        cachedSignupJob?.cancel()
        cachedSignupJob = viewModelScope.launch {
            _hideSoftKeyboardRequest.emit(Unit)
            val em = email.value!!
            val pw = password.value!!
            val rpw = repeatPassword.value!!
            when {
                pw != rpw -> {
                }
                User.signup(User(em, pw)) -> {
                    _navigationRequest.emit(SignupFragmentDirections.actionSignupToLogin())
                }
                else -> {
                    _errorToast.emit(R.string.account_already_exists)
                }
            }
        }
    }

    @MainThread
    fun clearAllInput() {
        email.value = ""
        password.value = ""
        repeatPassword.value = ""
    }
}
