package gst.trainingcourse.lesson7_8_ex1_congvc7.ui.bottom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import gst.trainingcourse.lesson7_8_ex1_congvc7.service.AudioManager
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.AudioListRequest
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.PlayPlaylistRequest

class BottomPlaybackVM(private val manager: AudioManager) : ViewModel(), AudioManager by manager {

    class Factory(private val manager: AudioManager) : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BottomPlaybackVM(manager) as T
        }
    }

    fun handleAudioListRequest(request: AudioListRequest) {
        when (request) {
            is PlayPlaylistRequest -> {
                loadPlaylist(request.playlist)
                request.startAudioId?.let(this::playAudioWithId)
            }
        }
    }

    fun togglePlayPause(){
        when(isPlaying.value){
            true -> pause()
            false -> play()
        }
    }
}
