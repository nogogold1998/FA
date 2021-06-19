package gst.trainingcourse.advancedlesson78_congvc7.util

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun Activity.hideSoftKeyboard(): Boolean {
    val view = currentFocus ?: return false
    val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)!!
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    return true
}
