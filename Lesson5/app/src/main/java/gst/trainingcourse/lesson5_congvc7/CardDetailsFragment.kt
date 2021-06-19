package gst.trainingcourse.lesson5_congvc7

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull

class CardDetailsFragment : Fragment(R.layout.fragment_card_details) {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val onDestroyViewJobs = mutableListOf<Job>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.run {
            val cardNumberTv: TextView = findViewById(R.id.details_text_card_number)
            val expiryDateTv: TextView = findViewById(R.id.details_text_expiry_date)
            val holderNameTv: TextView = findViewById(R.id.details_text_holder_name)

            lifecycleScope.launchWhenStarted {
                sharedViewModel.showingCard
                    .filterNotNull()
                    .collect {
                        cardNumberTv.text = it.number
                        expiryDateTv.text = it.expiryDate
                        holderNameTv.text = it.holderName
                    }
            }

            findViewById<Button>(R.id.details_button_update).setOnClickListener {
                val card = sharedViewModel.showingCard.value ?: return@setOnClickListener
                showCardEditFragment(card)
                listenToResult()
            }

            findViewById<Button>(R.id.details_button_close).setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun showCardEditFragment(card: Card) {
        CardEditFragment.getInstance(card.toBundle())
            .show(childFragmentManager, CardEditFragment.TAG)
    }

    private fun listenToResult() {
        childFragmentManager.setFragmentResultListener(
            CardEditFragment.REQUEST_EDIT_CARD,
            this@CardDetailsFragment
        ) { request: String, bundle: Bundle ->
            if (request == CardEditFragment.REQUEST_EDIT_CARD) {
                val result = Card.fromBundle(bundle) ?: return@setFragmentResultListener
                sharedViewModel.save(result)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyViewJobs.run {
            forEach(Job::cancel)
            clear()
        }
    }
}
