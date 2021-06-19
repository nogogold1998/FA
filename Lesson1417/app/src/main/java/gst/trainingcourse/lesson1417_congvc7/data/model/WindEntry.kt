package gst.trainingcourse.lesson1417_congvc7.data.model

import com.google.gson.annotations.SerializedName

data class WindEntry(
    val speed: Float = 0f,
    @SerializedName("deg")
    val degree: Int = 0,
)
