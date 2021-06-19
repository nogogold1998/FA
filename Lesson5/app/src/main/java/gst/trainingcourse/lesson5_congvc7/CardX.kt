package gst.trainingcourse.lesson5_congvc7

import android.os.Bundle
import androidx.core.os.bundleOf

private const val CONTAINS_A_CARD = "CONTAINS_A_CARD"
private const val ID = "ID"
private const val HOLDER_NAME = "HOLDER_NAME"
private const val EXPIRY_DATE = "EXPIRY_DATE"
private const val CARD_NUMBER = "CARD_NUMBER"

fun Card.toBundle(): Bundle {
    return bundleOf(
        CONTAINS_A_CARD to true,
        ID to id,
        HOLDER_NAME to holderName,
        EXPIRY_DATE to expiryDate,
        CARD_NUMBER to number,
    )
}

fun Card.Companion.fromBundle(bundle: Bundle): Card? = bundle.run {
    if (getBoolean(CONTAINS_A_CARD)) {
        Card(
            getString(HOLDER_NAME)!!,
            getString(EXPIRY_DATE)!!,
            getString(CARD_NUMBER)!!,
            getInt(ID)
        )
    } else {
        null
    }
}
