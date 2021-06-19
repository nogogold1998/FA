package gst.trainingcourse.lesson1417_congvc7.ui.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import gst.trainingcourse.lesson1417_congvc7.util.UnitSystem

@BindingAdapter("temperature")
fun TextView.bindingTemperature(temperature: Float?) {
    if (temperature == null) return
    text = UnitSystem.formatTemperature(temperature, resources)
}

@BindingAdapter("pressure")
fun TextView.bindingPressure(pressure: Int?) {
    if (pressure == null) return
    text = UnitSystem.formatPressure(pressure, resources)
}

@BindingAdapter("percentage")
fun TextView.bindingHumidity(percentage: Int?) {
    if (percentage == null) return
    text = UnitSystem.formatPercentageNumber(percentage.toFloat(), resources)
}
