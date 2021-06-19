package gst.trainingcourse.lesson7_8_ex1_congvc7.service

import android.app.NotificationManager
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.coroutineScope
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.Audio
import gst.trainingcourse.lesson7_8_ex1_congvc7.di.Injector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class AudioService : LifecycleService() {
    inner class MediaBinder : Binder() {
        fun getMediaService(): AudioService = this@AudioService
    }

    private val binder = MediaBinder()
    private val audioNotificationManager = Injector.audioNotificationManager
    private val notificationManager: NotificationManager by lazy {
        ContextCompat.getSystemService(baseContext, NotificationManager::class.java)!!
    }
    private val audioManager: AudioManager = Injector.audioManager.also {
        lifecycle.addObserver(it)
    }

    private val notificationAudioFlow: Flow<Pair<Audio, Boolean>> = combine(
        audioManager.currentAudio.filterNotNull(),
        audioManager.isPlaying
    ) { audio, isPlaying ->
        audio to isPlaying
    }

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        audioNotificationManager.createAudioNotificationChannel()
        lifecycle.coroutineScope.launch {
            notificationAudioFlow.collect { (audio, isPlaying) ->
                val notification =
                    audioNotificationManager.buildAudioNotification(audio, isPlaying)
                when (isPlaying) {
                    true -> startForeground(FOREGROUND_NOTIFICATION_PLAYBACK_ID, notification)
                    false -> {
                        notificationManager.notify(
                            FOREGROUND_NOTIFICATION_PLAYBACK_ID,
                            notification
                        )
                        stopForeground(false)
                    }
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (intent != null) {
            when (intent.action) {
                ACTION_PLAYBACK_PLAY -> audioManager.play()
                ACTION_PLAYBACK_PAUSE -> audioManager.pause()
                ACTION_PLAYBACK_NEXT -> audioManager.next()
                ACTION_PLAYBACK_PREVIOUS -> audioManager.previous()
                ACTION_PLAYBACK_STOP -> stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        ContextCompat.getSystemService(this, NotificationManager::class.java)
            ?.cancel(FOREGROUND_NOTIFICATION_PLAYBACK_ID)
        super.onDestroy()
    }

    companion object {
        const val TAG = "MediaService"

        private const val FOREGROUND_NOTIFICATION_PLAYBACK_ID = 16

        const val ACTION_PLAYBACK_PLAY = "action.playback.play"
        const val ACTION_PLAYBACK_PAUSE = "action.playback.pause"
        const val ACTION_PLAYBACK_STOP = "action.playback.stop"
        const val ACTION_PLAYBACK_NEXT = "action.playback.next"
        const val ACTION_PLAYBACK_PREVIOUS = "action.playback.previous"
    }
}
