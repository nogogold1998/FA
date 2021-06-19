package gst.trainingcourse.lesson10_ex1_congvc7

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log

class ClientService : Service() {

    private var serverMessenger: Messenger? = null

    private val clientHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                IpcContract.RECEIVE_MESSAGE_CODE -> {
                    val answer = msg.data.getString(IpcContract.RECEIVE_MESSAGE_KEY_ANSWER)!!
                    Log.d(TAG, "handleMessage: answer=$answer")
                    postDelayed({ sendRandomKey() }, IpcContract.DELAY_INTERVAL)
                }
            }
        }
    }

    private val messenger = Messenger(clientHandler)

    private val serverConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serverMessenger = Messenger(service)
            sendRandomKey()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serverMessenger = null
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return when (intent?.action) {
            ACTION_START_COMMUNICATION -> {
                bindService(ServerService.getIntent(this), serverConnection, BIND_AUTO_CREATE)
                START_NOT_STICKY
            }
            else -> {
                super.onStartCommand(intent, flags, startId)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder? = null

    private fun sendRandomKey() {
        val r = IpcContract.keys.random()
        val msg = Message.obtain(null, r).apply {
            replyTo = messenger
        }
        serverMessenger!!.send(msg)
        Log.d(TAG, "sendRandomKey: key=$r")
    }

    companion object {
        const val TAG = "ClientService"

        const val ACTION_START_COMMUNICATION = "ACTION_START_COMMUNICATION"

        fun getIntent(context: Context) = Intent(context, ClientService::class.java)
    }
}
