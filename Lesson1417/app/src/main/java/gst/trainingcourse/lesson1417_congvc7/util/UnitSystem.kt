package gst.trainingcourse.lesson1417_congvc7.util

import android.content.res.Resources
import gst.trainingcourse.lesson1417_congvc7.R
import gst.trainingcourse.lesson1417_congvc7.util.Constants.KILOPASCAL_TO_HECTOPASCAL

object UnitSystem {
    fun formatPressure(pressure: Int, resources: Resources): String {
        return resources.getString(
            R.string.format_pressure,
            pressure.toFloat() / KILOPASCAL_TO_HECTOPASCAL
        )
    }

    fun formatSpeed(speed: Float, resources: Resources): String {
        return resources.getString(R.string.format_speed_metric, speed)
    }

    fun formatDistance(distance: Int, resources: Resources): String {
        return resources.getString(R.string.format_distance_metric, distance)
    }

    fun formatTemperature(temperature: Float, resources: Resources): String {
        return resources.getString(R.string.format_temperature_celsius, temperature)
    }

    fun formatPercentageNumber(percentage: Float, resources: Resources): String {
        return resources.getString(R.string.format_percentage_decimal, percentage)
    }
}
