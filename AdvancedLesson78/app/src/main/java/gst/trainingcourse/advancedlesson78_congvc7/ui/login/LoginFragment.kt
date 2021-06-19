package gst.trainingcourse.advancedlesson78_congvc7.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import gst.trainingcourse.advancedlesson78_congvc7.databinding.FragmentLoginBinding
import gst.trainingcourse.advancedlesson78_congvc7.ui.base.ViewBindingFragment
import gst.trainingcourse.advancedlesson78_congvc7.ui.main.MainViewModel
import kotlinx.coroutines.flow.collect

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>() {
    private val viewModel by viewModels<LoginViewModel>()
    private val mainViewModel by activityViewModels<MainViewModel>()

    override val inflateRef: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            viewModel.errorToast.collect {
                Toast.makeText(requireContext(), resources.getString(it), Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.hideSoftKeyboard.collectWhenStarted {
            mainViewModel.hideSoftKeyboard()
        }
        viewModel.navigateRequest.collectWhenStarted {
            findNavController().navigate(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.textInputEmail.setEndIconOnClickListener {
            viewModel.clearAllInput()
        }
    }
}
