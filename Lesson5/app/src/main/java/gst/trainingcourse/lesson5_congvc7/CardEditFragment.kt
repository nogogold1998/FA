package gst.trainingcourse.lesson5_congvc7

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult

class CardEditFragment : DialogFragment(R.layout.fragment_card_edit) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.run {
            val card = arguments?.let(Card::fromBundle) ?: return@run
            val holderNameEt: EditText = findViewById(R.id.edit_edit_holder_name)
            val cardNumberEt: EditText = findViewById(R.id.edit_edit_card_number)
            val expiryDate: EditText = findViewById(R.id.edit_edit_expiry_date)

            holderNameEt.text.append(card.holderName)
            cardNumberEt.text.append(card.number)
            expiryDate.text.append(card.expiryDate)
            findViewById<Button>(R.id.edit_button_save).setOnClickListener {
                setFragmentResult(
                    REQUEST_EDIT_CARD,
                    Card(
                        holderNameEt.text.toString(),
                        expiryDate.text.toString(),
                        cardNumberEt.text.toString(),
                        card.id
                    ).toBundle()
                )
                dismiss()
            }
        }
    }

    companion object {
        const val TAG = "CardEditFragment"

        const val REQUEST_EDIT_CARD = "REQUEST_EDIT_CARD"

        fun getInstance(args: Bundle) = CardEditFragment().also { it.arguments = args }
    }
}
