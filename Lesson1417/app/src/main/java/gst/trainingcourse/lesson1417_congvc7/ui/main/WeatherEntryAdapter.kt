package gst.trainingcourse.lesson1417_congvc7.ui.main

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.lesson1417_congvc7.data.model.WeatherEntry
import gst.trainingcourse.lesson1417_congvc7.databinding.ItemWeatherEntryBinding
import gst.trainingcourse.lesson1417_congvc7.util.layoutInflater

private val weatherEntryDiffUtil = object : DiffUtil.ItemCallback<WeatherEntry>() {
    override fun areItemsTheSame(old: WeatherEntry, new: WeatherEntry): Boolean {
        return old.id == new.id
    }

    override fun areContentsTheSame(old: WeatherEntry, new: WeatherEntry): Boolean {
        return old == new
    }
}

class WeatherEntryAdapter : ListAdapter<WeatherEntry, WeatherEntryAdapter.WeatherEntryVH>(
    weatherEntryDiffUtil
) {
    class WeatherEntryVH private constructor(
        private val binding: ItemWeatherEntryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherEntry) {
            binding.weatherEntry = item
            binding.executePendingBindings()
        }

        companion object {
            operator fun invoke(parent: ViewGroup): WeatherEntryVH {
                val binding = ItemWeatherEntryBinding.inflate(parent.layoutInflater, parent, false)
                return WeatherEntryVH(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherEntryVH {
        return WeatherEntryVH(parent)
    }

    override fun onBindViewHolder(holder: WeatherEntryVH, position: Int) {
        getItem(position).let(holder::bind)
    }
}
