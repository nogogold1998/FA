package gst.trainingcourse.lesson1417_congvc7.data.model

import com.google.gson.annotations.SerializedName

data class MainEntry(
    @SerializedName("temp")
    val temperature: Float = 0f,
    @SerializedName("feels_like")
    val feelsLikeTemperature: Float = 0f,
    val pressure: Int = 0,
    val humidity: Int = 0,
)
