package gst.trainingcourse.lesson7_8_ex1_congvc7.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import gst.trainingcourse.lesson7_8_ex1_congvc7.R
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.Audio
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.LocalAudio
import gst.trainingcourse.lesson7_8_ex1_congvc7.service.AudioService
import kotlin.reflect.KClass

private const val REQUEST_CODE = 0b0110_1010

/**
 * Need even more abstraction >:(
 */
class AudioNotificationManager(
    private val targetActivityCls: KClass<out Activity>,
    private val targetServiceCls: KClass<out Service>,
    private val context: Context,
) {

    fun createAudioNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.getSystemService(context, NotificationManager::class.java)?.let {
                val channelId = context.getString(R.string.now_playing_channel_id)
                if (it.getNotificationChannel(channelId) == null) {
                    val channel = NotificationChannel(
                        channelId,
                        context.getString(R.string.now_playing_channel_name),
                        NotificationManager.IMPORTANCE_DEFAULT
                    ).apply {
                        description = context.getString(R.string.now_playing_channel_description)
                        setSound(null, null)
                    }
                    it.createNotificationChannel(channel)
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private val _cachedNotificationBuilder = NotificationCompat.Builder(
        context,
        context.getString(R.string.now_playing_channel_id)
    ).apply {
        run {
            val intent = Intent(context, targetActivityCls.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            setContentIntent(pendingIntent)
        }
        setDeleteIntent(
            buildAudioServicePendingIntent(AudioService.ACTION_PLAYBACK_STOP)
        )
        setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        setSmallIcon(R.drawable.ic_launcher_foreground)
        color = ContextCompat.getColor(
            context,
            R.color.teal_700
        )

        mActions.add(
            0,
            NotificationCompat.Action(
                R.drawable.ic_round_skip_previous,
                context.getString(R.string.skip_previous),
                buildAudioServicePendingIntent(AudioService.ACTION_PLAYBACK_PREVIOUS)
            )
        )
        mActions.add(null)
        mActions.add(
            2,
            NotificationCompat.Action(
                R.drawable.ic_round_skip_next,
                context.getString(R.string.skip_next),
                buildAudioServicePendingIntent(AudioService.ACTION_PLAYBACK_NEXT)
            )
        )
        setStyle(
            androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(1)
                .setShowCancelButton(true)
                .setCancelButtonIntent(
                    buildAudioServicePendingIntent(AudioService.ACTION_PLAYBACK_STOP)
                )
        )
    }

    @SuppressLint("RestrictedApi")
    fun buildAudioNotification(
        audio: Audio,
        isPlay: Boolean,
    ): Notification {
        val builder = _cachedNotificationBuilder.apply {
            if (audio is LocalAudio) {
                setContentTitle(audio.title)
                setContentText(audio.artist)
                setSubText(audio.album)
                setLargeIcon(audio.coverImage)
            }
            mActions[1] = if (isPlay) {
                NotificationCompat.Action(
                    R.drawable.ic_round_pause,
                    context.getString(R.string.pause),
                    buildAudioServicePendingIntent(AudioService.ACTION_PLAYBACK_PAUSE)
                )
            } else {
                NotificationCompat.Action(
                    R.drawable.ic_round_play,
                    context.getString(R.string.play),
                    buildAudioServicePendingIntent(AudioService.ACTION_PLAYBACK_PLAY)
                )
            }
        }
        return builder.build()
    }

    private fun buildAudioServicePendingIntent(
        action: String,
    ): PendingIntent = Intent(context, targetServiceCls.java).let {
        it.action = action
        PendingIntent.getService(
            context.applicationContext,
            REQUEST_CODE,
            it,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
