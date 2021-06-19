package gst.trainingcourse.lesson10_ex1_congvc7

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import androidx.core.os.bundleOf

class ServerService : Service() {

    private val serverHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                in answers.keys -> {
                    val answer = answers[msg.what]!!
                    val reply = Message.obtain(null, IpcContract.RECEIVE_MESSAGE_CODE).apply {
                        data = bundleOf(IpcContract.RECEIVE_MESSAGE_KEY_ANSWER to answer)
                    }
                    Log.d("TAG", "handleMessage: $answer")
                    val receiver = msg.replyTo
                    postDelayed(
                        { receiver.send(reply) },
                        IpcContract.DELAY_INTERVAL
                    )
                }
                else -> {
                    super.handleMessage(msg)
                }
            }
        }
    }

    private val answers = mapOf(
        IpcContract.ZERO to "Window",
        IpcContract.ONE to "Linux",
        IpcContract.TWO to "Mac",
        IpcContract.FOUR to "Unix",
        IpcContract.FIVE to "Android"
    )

    override fun onBind(intent: Intent): IBinder? {
        return Messenger(serverHandler).binder
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, ServerService::class.java)
    }
}

