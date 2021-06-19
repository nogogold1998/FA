package gst.trainingcourse.lesson1417_congvc7.data.source.remote

object ApiConstants {

    const val SCHEME = "https"

    const val AUTHORITY_OPENWEATHERMAP_API = "api.openweathermap.org"

    private const val PATH_DATA = "data"
    private const val PATH_2_5 = "2.5"

    const val BASE_URL: String = "$SCHEME://$AUTHORITY_OPENWEATHERMAP_API"

    object Query {
        const val UNITS = "units"
        const val API_KEY = "appid"
        const val LANGUAGE = "lang"
    }

    object Values {
        enum class Units {
            STANDARD, METRIC, IMPERIAL
        }

        enum class Language(val qValue: String) {
            ENGLISH("en"),
            VIETNAMESE("vi")
        }

        const val API_KEY = "92dde9ee49304049559090f694cd3d42"
    }

    object CurrentWeather {
        const val PATH = "/$PATH_DATA/$PATH_2_5/weather"

        const val QUERY_CITY_NAME = "q"
    }

    object Icons {
        fun getUrl(iconCode: String): String = "http://openweathermap.org/img/wn/$iconCode@2x.png"
    }
}
