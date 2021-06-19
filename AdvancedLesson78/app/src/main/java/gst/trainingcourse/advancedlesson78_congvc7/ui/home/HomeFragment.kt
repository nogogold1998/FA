package gst.trainingcourse.advancedlesson78_congvc7.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import gst.trainingcourse.advancedlesson78_congvc7.databinding.FragmentHomeBinding
import gst.trainingcourse.advancedlesson78_congvc7.ui.base.ViewBindingFragment
import gst.trainingcourse.advancedlesson78_congvc7.ui.main.MainViewModel

class HomeFragment : ViewBindingFragment<FragmentHomeBinding>() {
    private val mainViewModel by activityViewModels<MainViewModel>()

    private val navArgs by navArgs<HomeFragmentArgs>()

    override val inflateRef: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.loadUser(navArgs.email)
    }

    override fun observeLiveData() {
        mainViewModel.email.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            binding.textEmail.text = it
        }
    }
}
