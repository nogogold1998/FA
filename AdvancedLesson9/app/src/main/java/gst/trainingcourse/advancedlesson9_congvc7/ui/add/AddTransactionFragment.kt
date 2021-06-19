package gst.trainingcourse.advancedlesson9_congvc7.ui.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import gst.trainingcourse.advancedlesson9_congvc7.R
import gst.trainingcourse.advancedlesson9_congvc7.databinding.FragmentAddTransactionBinding
import gst.trainingcourse.advancedlesson9_congvc7.di.Injector
import gst.trainingcourse.advancedlesson9_congvc7.ui.base.ViewBindingFragment
import gst.trainingcourse.advancedlesson9_congvc7.util.hideSoftKeyboard

class AddTransactionFragment : ViewBindingFragment<FragmentAddTransactionBinding>() {
    override val inflateRef: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAddTransactionBinding
        get() = FragmentAddTransactionBinding::inflate

    private val viewModel: AddTransactionViewModel by viewModels(factoryProducer = Injector::addTransactionVMFactory)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.addTransactionVM = viewModel

        binding.textInputAmount.addTextChangedListener {
            viewModel.changeAmount(it?.toString())
        }

        requireActivity()
            .findViewById<FloatingActionButton>(R.id.main_floating_action_button)
            .apply {
                setImageResource(R.drawable.ic_baseline_check_24)
                setOnClickListener {
                    viewModel.addTransaction()
                }
            }

        viewModel.navigateToHomeRequest.collectWhenStarted {
            requireActivity().hideSoftKeyboard()
            val action = AddTransactionFragmentDirections.actionAddTransactionToHome()
            findNavController().navigate(action)
        }
    }
}
