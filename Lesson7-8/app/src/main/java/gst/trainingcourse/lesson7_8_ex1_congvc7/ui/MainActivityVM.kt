package gst.trainingcourse.lesson7_8_ex1_congvc7.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.Audio
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MainActivityVM : ViewModel() {
    private val _request = MutableSharedFlow<AudioListRequest>()

    val listAudioRequest: SharedFlow<AudioListRequest>
        get() = _request

    fun requestPlayBack(playlist: List<Audio>, startAudioId: Long? = null) {
        viewModelScope.launch {
            _request.emit(PlayPlaylistRequest(playlist, startAudioId))
        }
    }
}

sealed class AudioListRequest
data class PlayPlaylistRequest(
    val playlist: List<Audio>,
    val startAudioId: Long? = null,
) : AudioListRequest()
