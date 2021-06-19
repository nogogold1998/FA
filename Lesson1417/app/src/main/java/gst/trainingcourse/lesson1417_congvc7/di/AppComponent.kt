package gst.trainingcourse.lesson1417_congvc7.di

import dagger.Component
import gst.trainingcourse.lesson1417_congvc7.ui.main.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}
