package gst.trainingcourse.lesson7_8_ex1_congvc7.ui.bottom

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import gst.trainingcourse.lesson7_8_ex1_congvc7.R
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.LocalAudio
import gst.trainingcourse.lesson7_8_ex1_congvc7.databinding.FragmentBottomPlaybackBinding
import gst.trainingcourse.lesson7_8_ex1_congvc7.di.Injector
import gst.trainingcourse.lesson7_8_ex1_congvc7.service.AudioService
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.MainActivityVM
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.base.ViewBindingFragment
import kotlinx.coroutines.flow.collect

class BottomPlaybackFragment : ViewBindingFragment<FragmentBottomPlaybackBinding>() {

    private inner class AudioConnection : ServiceConnection, DefaultLifecycleObserver {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as? AudioService.MediaBinder
            audioService = binder?.getMediaService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            audioService = null
        }

        override fun onCreate(owner: LifecycleOwner) {
            val context = this@BottomPlaybackFragment.requireContext()
            val intent = Intent(context, AudioService::class.java)
            context.bindService(intent, this, Context.BIND_AUTO_CREATE)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            val context = requireContext()
            if (bottomPlaybackVM.isPlaying.value) {
                context.startService(Intent(context, AudioService::class.java))
            }
            context.unbindService(this)
        }
    }

    private val mainVM: MainActivityVM by activityViewModels()

    private val bottomPlaybackVM: BottomPlaybackVM
        by viewModels { Injector.provideBottomPlaybackVMFactory() }

    private var audioService: AudioService? = null
    private val serviceConnection =
        AudioConnection().also(lifecycle::addObserver)

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentBottomPlaybackBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonNext.setOnClickListener {
            bottomPlaybackVM.next()
        }
        binding.buttonPlayPause.setOnClickListener {
            bottomPlaybackVM.togglePlayPause()
        }
        binding.buttonPrevious.setOnClickListener {
            bottomPlaybackVM.previous()
        }
        binding.audioProgressSeekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?, progress: Int, fromUser: Boolean
                ) {
                    bottomPlaybackVM.seekTo(progress.toLong())
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            })
    }

    override fun observeLiveData() {
        lifecycleScope.launchWhenStarted {
            mainVM.listAudioRequest.collect {
                bottomPlaybackVM.handleAudioListRequest(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            bottomPlaybackVM.currentAudio.collect {
                if (it == null) return@collect
                when (it) {
                    is LocalAudio -> {
                        binding.audioArtist.text = it.artist
                        binding.audioDuration.text = DateUtils.formatElapsedTime(it.duration / 1000)
                        binding.audioTitle.text = it.title
                        binding.audioProgressSeekBar.max = it.duration.toInt()
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            bottomPlaybackVM.isPlaying.collect {
                binding.buttonPlayPause.setImageResource(
                    if (it) R.drawable.ic_round_pause else R.drawable.ic_round_play
                )
            }
        }
        lifecycleScope.launchWhenStarted {
            bottomPlaybackVM.tickerMillis.collect {
                binding.textCurrent.text = DateUtils.formatElapsedTime(it / 1000)
                binding.audioProgressSeekBar.progress = it.toInt()
            }
        }
    }
}
