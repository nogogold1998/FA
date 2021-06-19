package gst.trainingcourse.advancedlesson12_congvc7

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import gst.trainingcourse.advancedlesson12_congvc7.contract.IRemoteTrainee
import gst.trainingcourse.advancedlesson12_congvc7.contract.IRemoteTraineeCallback
import gst.trainingcourse.advancedlesson12_congvc7.contract.MarkEntry
import gst.trainingcourse.advancedlesson12_congvc7.contract.Trainee

const val TAG = "TAG"

class MainActivity : AppCompatActivity() {
    private var remoteTrainee: IRemoteTrainee? = null

    private val remoteConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            remoteTrainee = IRemoteTrainee.Stub.asInterface(service)
            remoteTrainee!!.registerBestTrainee(object : IRemoteTraineeCallback.Stub() {
                override fun onChanged(value: Trainee?) {
                    Log.d(TAG, "onChanged: $value")
                    Log.d(TAG, "onChanged: ${Thread.currentThread().name}")
                }
            })
            Log.d(TAG, "onServiceConnected: ${Thread.currentThread().name}")
            remoteTrainee!!.add(
                arrayOf(
                    Trainee(
                        0,
                        "Cong",
                        12,
                        "An1",
                        MarkEntry(1f, 1f, 4f, 1f, 4f)
                    )
                )
            )
        }

        override fun onServiceDisconnected(name: ComponentName) {
            remoteTrainee = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: ${Thread.currentThread().name}")
        val intent = Intent().apply {
            this.component = ComponentName(
                "gst.trainingcourse.advancedlesson12_congvc7.server",
                "gst.trainingcourse.advancedlesson12_congvc7.server.ServerService"
            )
        }
        bindService(intent, remoteConnection, Service.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(remoteConnection)
    }
}
