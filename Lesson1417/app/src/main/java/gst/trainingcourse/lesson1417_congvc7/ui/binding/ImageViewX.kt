package gst.trainingcourse.lesson1417_congvc7.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import gst.trainingcourse.lesson1417_congvc7.data.source.remote.ApiConstants
import gst.trainingcourse.lesson1417_congvc7.util.load

@BindingAdapter("weatherIcon")
fun ImageView.bindWeatherIcon(icon: String?) {
    if (icon == null) return
    val url = ApiConstants.Icons.getUrl(icon)
    load(url)
}
