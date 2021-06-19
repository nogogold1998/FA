package gst.trainingcourse.lesson7_8_ex1_congvc7.service

import androidx.lifecycle.LifecycleObserver
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.Audio
import kotlinx.coroutines.flow.StateFlow

interface AudioManager : LifecycleObserver {
    enum class PlayMode {
        NONE, REPEAT_ONCE, PLAY_LIST, CURRENT_PLAYING
    }

    val tickerMillis: StateFlow<Long>
    val currentPlaylist: StateFlow<List<Audio>>
    val currentAudio: StateFlow<Audio?>
    val isPlaying: StateFlow<Boolean>
    val mode: StateFlow<PlayMode>

    fun next()
    fun previous()
    fun play()
    fun pause()
    fun playAudioWithId(id: Long)
    fun seekTo(millisSec: Long)
    fun loadPlaylist(list: List<Audio>)
}
