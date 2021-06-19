package gst.trainingcourse.congvc7_advancedandroid_final.server.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import gst.trainingcourse.congvc7_advancedandroid_final.api.IServer

private const val OPERATION_COUNT_FROM = 1
private const val OPERATION_COUNT_TO = 1000

private const val PROCESS_DELAY_INTERVAL_MILLIS = 5000L

class ServerService : Service() {

    private val binder by lazy {
        object : IServer.Stub() {
            override fun run(): String {
                operation()
                process()
                return locateMe()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(TAG, "onBind: ")
        return binder
    }

    private fun operation() {
        (OPERATION_COUNT_FROM..OPERATION_COUNT_TO).forEach {
            Log.d(TAG, "operation: count:$it")
        }
    }

    private fun process() {
        try {
            Thread.sleep(PROCESS_DELAY_INTERVAL_MILLIS)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun locateMe(): String = "ZONE3 - ${System.currentTimeMillis()}"

    companion object {
        private val TAG = ServerService::class.java.simpleName
    }
}
