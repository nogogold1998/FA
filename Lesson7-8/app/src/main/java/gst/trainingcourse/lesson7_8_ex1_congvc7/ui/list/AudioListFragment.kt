package gst.trainingcourse.lesson7_8_ex1_congvc7.ui.list

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import gst.trainingcourse.lesson7_8_ex1_congvc7.R
import gst.trainingcourse.lesson7_8_ex1_congvc7.databinding.FragmentAudioListBinding
import gst.trainingcourse.lesson7_8_ex1_congvc7.di.Injector
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.MainActivityVM
import gst.trainingcourse.lesson7_8_ex1_congvc7.ui.base.ViewBindingFragment
import kotlinx.coroutines.flow.collect

class AudioListFragment : ViewBindingFragment<FragmentAudioListBinding>() {
    private val mainVM: MainActivityVM by activityViewModels()
    private val audioListVM: AudioListVM by lazy { Injector.provideAudioListVM() }
    private val adapter: AudioAdapter by lazy {
        AudioAdapter {
            mainVM.requestPlayBack(audioListVM.audioList.value, it.id)
        }
    }

    @SuppressLint("MissingPermission")
    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            audioListVM.fetchLocalAudios()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAudioListBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.audioRecycler.adapter = adapter
        lifecycleScope.launchWhenStarted { saveFetchLocalAudio() }
    }

    override fun observeLiveData() {
        lifecycleScope.launchWhenStarted {
            audioListVM.audioList.collect {
                adapter.submitList(it)
            }
        }
    }

    private fun saveFetchLocalAudio() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                READ_EXTERNAL_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
                audioListVM.fetchLocalAudios()
            }
            shouldShowRequestPermissionRationale(READ_EXTERNAL_PERMISSION) -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.need_read_external_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                permissionRequest.launch(READ_EXTERNAL_PERMISSION)
            }
        }
    }
}

const val READ_EXTERNAL_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE
