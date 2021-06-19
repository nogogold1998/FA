package gst.trainingcourse.lesson7_8_ex1_congvc7.data.local

import android.content.Context
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.LocalAudio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface LocalAudioSource {
    fun getAllLocalAudios(context: Context): Flow<List<LocalAudio>>

    fun getAllLocalAudiosAsync(context: Context, scope: CoroutineScope): Deferred<List<LocalAudio>>
}
