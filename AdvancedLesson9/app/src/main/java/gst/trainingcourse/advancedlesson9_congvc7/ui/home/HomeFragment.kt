package gst.trainingcourse.advancedlesson9_congvc7.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import gst.trainingcourse.advancedlesson9_congvc7.R
import gst.trainingcourse.advancedlesson9_congvc7.databinding.FragmentHomeBinding
import gst.trainingcourse.advancedlesson9_congvc7.di.Injector
import gst.trainingcourse.advancedlesson9_congvc7.ui.base.ViewBindingFragment
import gst.trainingcourse.advancedlesson9_congvc7.util.verticalScrollProgress

class HomeFragment : ViewBindingFragment<FragmentHomeBinding>() {
    override val inflateRef: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    private val transactionAdapter by lazy {
        TransactionAdapter(swipeListener = viewModel::removeTransaction)
    }

    private val viewModel by viewModels<HomeViewModel>(factoryProducer = Injector::homeVMFactory)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_delete_all_transaction) {
            viewModel.deleteAllTransactions()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding()

        val mainFab = requireActivity()
            .findViewById<FloatingActionButton>(R.id.main_floating_action_button)

        setupMainFab(mainFab)
        setupRecyclerView(mainFab)
    }

    private fun setupBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.homeVM = viewModel
        binding.transactionAdapter = transactionAdapter
        binding.executePendingBindings()
    }

    private fun setupMainFab(mainFab: FloatingActionButton) {
        lifecycleScope.launchWhenStarted {
            mainFab.apply {
                    setImageResource(R.drawable.ic_baseline_add_24)
                    setOnClickListener {
                        val action = HomeFragmentDirections.actionHomeToAddTransaction()
                        findNavController().navigate(action)
                    }
                }
        }
    }

    private fun setupRecyclerView(mainFab: FloatingActionButton) {
        binding.recyclerTransaction.apply {

            val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.END) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean = false

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    (viewHolder as? TransactionAdapter.TransactionVH)?.onSwipe()
                }
            }
            val itemTouchHelper = ItemTouchHelper(swipeCallback)
            itemTouchHelper.attachToRecyclerView(this)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (recyclerView.verticalScrollProgress > 0.9) {
                        mainFab.hide()
                    } else {
                        mainFab.show()
                    }
                }
            })
        }
    }

    override fun observeLiveData() {
        super.observeLiveData()

        viewModel.allTransactions.collectWhenStarted {
            transactionAdapter.submitList(it)
        }
        // viewModel.transactions.observe(viewLifecycleOwner) {
        //     transactionAdapter.submitList(it)
        // }

        var cachedSnackbar: Snackbar? = null
        val contextView = requireActivity().findViewById<View>(R.id.main_activity_context_view)
        viewModel.isLazilyRemovalDismissible.collectWhenStarted {
            if (it) {
                cachedSnackbar = Snackbar.make(
                    contextView,
                    R.string.transaction_removed,
                    Snackbar.LENGTH_INDEFINITE,
                ).setAction(R.string.undo) {
                    viewModel.undoRemoval()
                }.apply { show() }
            } else {
                cachedSnackbar?.dismiss()
            }
        }
    }
}
