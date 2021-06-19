package gst.trainingcourse.lesson5_congvc7

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

class CardListFragment : Fragment(R.layout.fragment_card_list) {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val adapter = CardAdapter {
        sharedViewModel.show(it.id)
    }

    private lateinit var recyclerView: RecyclerView

    private val onViewDestroyedJobs = mutableListOf<Job>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.adapter = adapter

        observeFlows()
    }

    private fun observeFlows() {
        lifecycleScope.run {
            onViewDestroyedJobs += launchWhenStarted {
                sharedViewModel.cardList.collect {
                    adapter.submitList(it) { adapter.notifyDataSetChanged() }
                }
            }
            onViewDestroyedJobs += launchWhenStarted {
                sharedViewModel.navigateToDetailsScreen.collect {
                    if (it) {
                        navigateToDetailsScreen()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onViewDestroyedJobs.run {
            forEach(Job::cancel)
            clear()
        }
    }

    private fun navigateToDetailsScreen() {
        requireActivity().supportFragmentManager.commit {
            replace(R.id.fragment_container_view, CardDetailsFragment())
            addToBackStack(null)
        }
        sharedViewModel.navigateToDetailScreenDone()
    }
}
