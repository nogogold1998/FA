package gst.trainingcourse.lesson1_ex1_congvc7.login

import gst.trainingcourse.lesson1_ex1_congvc7.R
import gst.trainingcourse.lesson1_ex1_congvc7.login.LoginContract.View.NavigateRequest.StartHomeActivity
import gst.trainingcourse.lesson1_ex1_congvc7.login.LoginContract.View.NotifyRequest.ShowErrDialog
import gst.trainingcourse.lesson1_ex1_congvc7.login.LoginContract.View.NotifyRequest.ToastStringRes
import kotlinx.coroutines.delay

const val CORRECT_USERNAME = "cong"
const val CORRECT_PASSWORD = "123456"
const val NETWORK_DELAY_INTERVAL = 1_000L

class LoginPresenterImpl(private val view: LoginContract.View) : LoginContract.Presenter {
    private var username: String = ""
    private var password: String = ""
    private var remember: Boolean = false

    override suspend fun login() {
        view.toggleProgressbarVisibility(true)
        when {
            password.length < 6 -> view.notify(ToastStringRes(R.string.login_msg_short_password))
            password != CORRECT_PASSWORD || username != CORRECT_USERNAME -> {
                delay(NETWORK_DELAY_INTERVAL)
                view.notify(ShowErrDialog(R.string.login_error_wrong_email_password))
            }
            else -> {
                delay(NETWORK_DELAY_INTERVAL)
                view.notify(ToastStringRes(R.string.login_msg_success))
                view.navigate(StartHomeActivity)
            }
        }
        view.toggleProgressbarVisibility(false)
    }

    override fun toggleRemember(remember: Boolean) {
        this.remember = remember
        view.updateRemember(remember)
    }

    override fun changeUsername(username: String) {
        this.username = username
        view.updateUsername(username)
    }

    override fun changePassword(password: String) {
        this.password = password
        view.updatePassword(password)
    }
}
