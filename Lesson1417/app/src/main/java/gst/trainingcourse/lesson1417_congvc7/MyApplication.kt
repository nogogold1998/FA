package gst.trainingcourse.lesson1417_congvc7

import android.app.Application
import gst.trainingcourse.lesson1417_congvc7.di.AppComponent
import gst.trainingcourse.lesson1417_congvc7.di.DaggerAppComponent

class MyApplication: Application() {
    val appComponent: AppComponent = DaggerAppComponent.create()
}
