package gst.trainingcourse.lesson1417_congvc7.data.model

import com.google.gson.annotations.SerializedName

data class CurrentWeatherEntry(
    val id: Int = 0,
    val name: String = "",
    @SerializedName("weather")
    val weathers: List<WeatherEntry> = emptyList(),
    @SerializedName("timezone")
    val timeZone: Long = 0L,
    val visibility: Int = 0,
    val wind: WindEntry = WindEntry(),
    @SerializedName("main")
    val mainInfo: MainEntry = MainEntry(),
)
