package gst.trainingcourse.lesson1417_congvc7.di

import dagger.Module
import dagger.Provides
import gst.trainingcourse.lesson1417_congvc7.data.source.remote.OpenWeatherApiService
import javax.inject.Singleton

@Module
object NetworkModule {
    @Provides
    @Singleton
    fun provideOpenWeatherApiService(): OpenWeatherApiService {
        return OpenWeatherApiService.create()
    }
}
