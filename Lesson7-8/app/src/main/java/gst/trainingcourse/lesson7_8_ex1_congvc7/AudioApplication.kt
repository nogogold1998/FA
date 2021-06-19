package gst.trainingcourse.lesson7_8_ex1_congvc7

import android.app.Application
import gst.trainingcourse.lesson7_8_ex1_congvc7.di.inject

class AudioApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        inject()
    }
}
