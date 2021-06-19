package gst.trainingcourse.lesson7_8_ex1_congvc7.di

import android.app.Application
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.local.LocalAudioSource
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.local.LocalAudioSourceImpl
import gst.trainingcourse.lesson7_8_ex1_congvc7.notification.AudioNotificationManager
import gst.trainingcourse.lesson7_8_ex1_congvc7.service.AudioManager
import gst.trainingcourse.lesson7_8_ex1_congvc7.service.AudioManagerImpl
import gst.trainingcourse.lesson7_8_ex1_congvc7.service.AudioService
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.MainActivity
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.bottom.BottomPlaybackVM
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.list.AudioListVM

private lateinit var mApplicationCtx: Application

private val targetAudioServiceCls = AudioService::class
private val targetActivityCls = MainActivity::class

object Injector {
    val audioNotificationManager: AudioNotificationManager by lazy {
        AudioNotificationManager(targetActivityCls, targetAudioServiceCls, mApplicationCtx)
    }

    val audioManager: AudioManager by lazy {
        AudioManagerImpl(targetAudioServiceCls)
    }

    private val localAudioSource: LocalAudioSource by lazy {
        LocalAudioSourceImpl()
    }

    fun provideAudioListVM(): AudioListVM = AudioListVM(mApplicationCtx, localAudioSource)
    fun provideBottomPlaybackVMFactory() = BottomPlaybackVM.Factory(audioManager)
}

fun Application.inject() {
    mApplicationCtx = this
}
