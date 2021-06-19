package gst.trainingcourse.advancedlesson78_congvc7.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import gst.trainingcourse.advancedlesson78_congvc7.databinding.FragmentSignupBinding
import gst.trainingcourse.advancedlesson78_congvc7.ui.base.ViewBindingFragment
import gst.trainingcourse.advancedlesson78_congvc7.ui.main.MainViewModel

class SignupFragment : ViewBindingFragment<FragmentSignupBinding>() {
    private val signupViewModel by viewModels<SignupViewModel>()
    private val mainVM by activityViewModels<MainViewModel>()

    override val inflateRef: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSignupBinding
        get() = FragmentSignupBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(signupViewModel) {
            navigationRequest.collectWhenStarted {
                findNavController().navigate(it)
            }
            errorToast.collectWhenStarted {
                Toast.makeText(requireContext(), resources.getString(it), Toast.LENGTH_SHORT).show()
            }
            hideSoftKeyboardRequest.collectWhenStarted {
                mainVM.hideSoftKeyboard()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupVM = signupViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.textInputEmail.setEndIconOnClickListener {
            signupViewModel.clearAllInput()
        }
    }
}
