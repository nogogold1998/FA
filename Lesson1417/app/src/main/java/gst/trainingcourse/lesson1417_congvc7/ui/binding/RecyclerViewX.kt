package gst.trainingcourse.lesson1417_congvc7.ui.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.lesson1417_congvc7.data.model.WeatherEntry
import gst.trainingcourse.lesson1417_congvc7.ui.main.WeatherEntryAdapter

@BindingAdapter("weatherEntryAdapter")
fun RecyclerView.bindWeatherEntryAdapter(adapter: WeatherEntryAdapter?) {
    if (adapter == null) return
    if (this.adapter === adapter) return
    this.adapter = adapter
}

@BindingAdapter("weatherEntries")
fun RecyclerView.bindListItem(items: List<WeatherEntry>?) {
    if (items == null) return
    (adapter as WeatherEntryAdapter).submitList(items)
}
