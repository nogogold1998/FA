package gst.trainingcourse.lesson7_8_ex1_congvc7.service

import android.app.Service
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.Audio
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.LocalAudio
import gst.trainingcourse.lesson7_8_ex1_congvc7.service.AudioManager.PlayMode
import gst.trainingcourse.lesson7_8_ex1_congvc7.service.AudioManager.PlayMode.NONE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Semaphore
import kotlin.reflect.KClass

private const val TICKER_INTERVAL = 100L

class AudioManagerImpl(
    private val targetAudioServiceClass: KClass<out Service>,
) : AudioManager, DefaultLifecycleObserver {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _playlist = MutableStateFlow(emptyList<Audio>())
    override val currentPlaylist: StateFlow<List<Audio>> = _playlist

    private val _playingIndex = MutableStateFlow(-1)

    override val currentAudio: StateFlow<Audio?> = _playingIndex
        .combine(currentPlaylist) { index, playlist ->
            playlist.getOrNull(index)
        }
        .stateIn(scope, SharingStarted.Eagerly, null)

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying

    private val _mode = MutableStateFlow(NONE)
    override val mode: StateFlow<PlayMode> = _mode

    private val _tickerMillis = MutableStateFlow(0L)
    override val tickerMillis: StateFlow<Long> = _tickerMillis

    init {
        // FIXME: Not practically implemented (simulate a audio player)
        val lock = Semaphore(1, 0)
        scope.launch {
            currentAudio.collect {
                lock.acquire()
                _tickerMillis.value = 0
                delay(TICKER_INTERVAL)
                lock.release()
            }
        }
        scope.launch {
            isPlaying.collect {
                delay(TICKER_INTERVAL)
                when (it) {
                    true -> lock.release()
                    false -> lock.acquire()
                }
            }
        }
        scope.launch {
            while (true) {
                lock.acquire()
                val audio = currentAudio.value
                if (audio is LocalAudio && audio.duration <= tickerMillis.value) next()
                _tickerMillis.value = _tickerMillis.value + TICKER_INTERVAL
                delay(TICKER_INTERVAL)
                lock.release()
            }
        }
    }

    override fun next() {
        if (_mode.value == NONE) {
            _playingIndex.value = _playingIndex.value + 1
        }
    }

    override fun previous() {
        if (_mode.value == NONE) {
            _playingIndex.value = _playingIndex.value - 1
        }
    }

    override fun play() {
        if (currentAudio.value != null) {
            _isPlaying.value = true
        }
    }

    override fun pause() {
        _isPlaying.value = false
    }

    override fun playAudioWithId(id: Long) {
        val index = currentPlaylist.value.indexOfFirst { it is LocalAudio && it.id == id }
        if (index == -1) return
        _playingIndex.value = index
        _isPlaying.value = true
    }

    override fun seekTo(millisSec: Long) {
        _tickerMillis.value = millisSec
    }

    override fun loadPlaylist(list: List<Audio>) {
        _playlist.value = list.toList()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        if (owner::class == targetAudioServiceClass) return
        scope.cancel()
        runBlocking {
            tickerMillis.collect { cancel() }
        }
    }
}
