package gst.trainingcourse.advancedlesson78_congvc7.ui.binding

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("error")
fun TextInputLayout.bindError(msg: String?) {
    error = msg
}

@BindingAdapter("error")
fun TextInputLayout.bindError(@StringRes strId: Int?) {
    error = when (strId) {
        null, Int.MIN_VALUE -> null
        else -> resources.getString(strId)
    }
}

@BindingMethods(
    value = [
        BindingMethod(
            type = TextView::class,
            attribute = "android:autoSizeMinTextSize",
            method = "setTextSize",
        )]
)
class TextViewCustomBindingMethods
