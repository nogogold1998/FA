package gst.trainingcourse.lesson9_ex1_congvc7

import android.app.Application
import gst.trainingcourse.lesson9_ex1_congvc7.di.inject

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        inject()
    }
}
