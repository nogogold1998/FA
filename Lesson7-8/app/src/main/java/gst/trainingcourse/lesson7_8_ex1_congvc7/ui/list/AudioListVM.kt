package gst.trainingcourse.lesson7_8_ex1_congvc7.ui.list

import android.app.Application
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.local.LocalAudioSource
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.Audio
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AudioListVM(
    app: Application,
    private val localAudioSource: LocalAudioSource,
) : AndroidViewModel(app) {
    private val _audioList = MutableStateFlow<List<Audio>>(emptyList())
    val audioList: StateFlow<List<Audio>> get() = _audioList

    private var _cachedFetchLocalAudiosJob: Job? = null

    @RequiresPermission(READ_EXTERNAL_PERMISSION)
    fun fetchLocalAudios() {
        _cachedFetchLocalAudiosJob?.cancel()
        _cachedFetchLocalAudiosJob = viewModelScope.launch {
            val newSource =
                localAudioSource
                    .getAllLocalAudios(getApplication())
                    .stateIn(viewModelScope)
            _audioList.emitAll(newSource)
        }
    }
}
