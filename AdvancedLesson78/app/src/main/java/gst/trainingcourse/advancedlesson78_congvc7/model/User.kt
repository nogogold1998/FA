package gst.trainingcourse.advancedlesson78_congvc7.model

import androidx.annotation.StringRes
import gst.trainingcourse.advancedlesson78_congvc7.R

private const val EMAIL_REGEX = """\w[\w|\d|_]+@\w[\w|\d]*(\.[\w]{1,5})+"""
private const val MIN_PASSWORD_LENGTH = 8

private const val CORRECT_EMAIL = "nogogold1998@live.com"
private const val CORRECT_PASSWORD = "12345678"

data class User(val email: String, val password: String) {
    companion object {
        private val users = mutableSetOf(User(CORRECT_EMAIL, CORRECT_PASSWORD))

        fun signup(user: User) = users.none { user.email == it.email }.also {
            if (it) users.add(user)
        }

        fun login(user: User) = users.contains(user)

        private val cachedRegex by lazy { EMAIL_REGEX.toRegex() }

        fun validateEmail(email: String) = cachedRegex.matches(email)

        fun validatePassword(password: String) = password.length >= MIN_PASSWORD_LENGTH

        @StringRes
        fun mapEmailError(it: String): Int = when {
            it.isEmpty() -> Int.MIN_VALUE
            !validateEmail(it) -> R.string.wrong_email_format
            else -> Int.MIN_VALUE
        }

        @StringRes
        fun mapPasswordError(it: String): Int = when {
            it.isEmpty() -> Int.MIN_VALUE
            !validatePassword(it) -> R.string.password_too_short
            else -> Int.MIN_VALUE
        }
    }
}
