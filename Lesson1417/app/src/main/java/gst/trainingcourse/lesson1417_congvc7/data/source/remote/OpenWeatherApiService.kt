package gst.trainingcourse.lesson1417_congvc7.data.source.remote

import gst.trainingcourse.lesson1417_congvc7.data.model.CurrentWeatherEntry
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    /**
     * note: api returns the same unix timestamp for each 10 minutes period
     * json has the same dateTime
     */
    @GET(ApiConstants.CurrentWeather.PATH)
    fun fetchCurrentWeather(
        @Query(ApiConstants.CurrentWeather.QUERY_CITY_NAME)
        cityName: String,
        @Query(ApiConstants.Query.UNITS)
        units: String = ApiConstants.Values.Units.METRIC.name,
        @Query(ApiConstants.Query.LANGUAGE)
        language: String = ApiConstants.Values.Language.ENGLISH.qValue
    ): Single<CurrentWeatherEntry>

    @GET(ApiConstants.CurrentWeather.PATH)
    suspend fun fetchCurrentWeatherSuspend(
        @Query(ApiConstants.CurrentWeather.QUERY_CITY_NAME)
        cityName: String,
        @Query(ApiConstants.Query.UNITS)
        units: String = ApiConstants.Values.Units.METRIC.name,
        @Query(ApiConstants.Query.LANGUAGE)
        language: String = ApiConstants.Values.Language.ENGLISH.qValue
    ): CurrentWeatherEntry

    companion object {

        fun create(url: String = ApiConstants.BASE_URL): OpenWeatherApiService = Retrofit.Builder()
            .baseUrl(url)
            .client(createOkHttpClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(OpenWeatherApiService::class.java)

        private fun createOkHttpClientBuilder() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                // add api key for each request
                val request = chain.request().newBuilder()
                val originalHttpUrl = chain.request().url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter(ApiConstants.Query.API_KEY, ApiConstants.Values.API_KEY)
                    .build()
                request.url(url)
                return@addInterceptor chain.proceed(request.build())
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
    }
}
