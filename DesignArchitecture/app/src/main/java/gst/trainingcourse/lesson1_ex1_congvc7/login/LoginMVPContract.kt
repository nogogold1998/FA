package gst.trainingcourse.lesson1_ex1_congvc7.login

import androidx.annotation.StringRes

object LoginContract {
    interface View {
        sealed class NotifyRequest {
            data class ToastStringRes(@StringRes val id: Int) : NotifyRequest()
            data class ShowErrDialog(@StringRes val id: Int) : NotifyRequest()
        }

        sealed class NavigateRequest {
            object StartHomeActivity : NavigateRequest()
        }

        fun toggleProgressbarVisibility(isShow: Boolean)

        fun notify(request: NotifyRequest)

        fun navigate(request: NavigateRequest)

        fun updateUsername(username: String)

        fun updateRemember(remember: Boolean)

        fun updatePassword(password: String)
    }

    interface Presenter {
        suspend fun login()

        fun toggleRemember(remember: Boolean)

        fun changeUsername(username: String)

        fun changePassword(password: String)
    }
}

