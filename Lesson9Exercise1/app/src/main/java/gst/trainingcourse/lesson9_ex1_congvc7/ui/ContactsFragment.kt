package gst.trainingcourse.lesson9_ex1_congvc7.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import gst.trainingcourse.lesson9_ex1_congvc7.R
import gst.trainingcourse.lesson9_ex1_congvc7.databinding.FragmentContactsBinding
import gst.trainingcourse.lesson9_ex1_congvc7.di.factory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Suppress("unused")
class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding: FragmentContactsBinding
        get() = _binding ?: throw IllegalStateException(
            "Must call after #onViewCreated(..) and before #onDestroyView(..)"
        )

    private val adapter: ContactAdapter by lazy { ContactAdapter() }

    private val permissionRequest: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            fetchContacts()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.permission_denied),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val viewModel: ContactsFragmentViewModel by viewModels(this::requireActivity) {
        ContactsFragmentViewModel.factory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentContactsBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        observeLiveData()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupViews() {
        binding.recyclerView.adapter = adapter
        binding.reFetchContactsButton.setOnClickListener {
            fetchContactsLazy()
        }
    }

    private fun observeLiveData() {
        lifecycleScope.launch {
            viewModel.contacts.collect {
                adapter.submitList(it) {
                    if (it.isNotEmpty()) {
                        binding.recyclerView.smoothScrollToPosition(it.lastIndex)
                    }
                }
            }
        }
    }

    private fun fetchContactsLazy() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                REQUIRED_PERMISSION
            ) == PackageManager.PERMISSION_GRANTED -> {
                fetchContacts()
            }
            shouldShowRequestPermissionRationale(REQUIRED_PERMISSION) -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.need_read_contacts_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                permissionRequest.launch(REQUIRED_PERMISSION)
            }
        }
    }

    private fun fetchContacts() {
        Toast.makeText(requireContext(), "fetching", Toast.LENGTH_SHORT).show()
        viewModel.fetchContacts()
    }

    companion object {
        const val REQUIRED_PERMISSION = Manifest.permission.READ_CONTACTS
    }
}
