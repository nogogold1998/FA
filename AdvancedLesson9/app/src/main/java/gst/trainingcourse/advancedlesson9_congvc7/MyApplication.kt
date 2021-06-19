package gst.trainingcourse.advancedlesson9_congvc7

import android.app.Application
import android.util.Log
import gst.trainingcourse.advancedlesson9_congvc7.di.inject

@Suppress("unused")
class MyApplication : Application() {
    override fun onCreate() {
        Log.d("TAG", "MyApplication#onCreate: ")
        super.onCreate()
        inject()
    }
}
