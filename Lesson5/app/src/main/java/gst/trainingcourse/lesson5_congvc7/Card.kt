package gst.trainingcourse.lesson5_congvc7

import androidx.recyclerview.widget.DiffUtil
import java.util.concurrent.atomic.AtomicInteger

data class Card(
    val holderName: String,
    val expiryDate: String,
    val number: String,
    val id: Int = idCount.getAndIncrement(),
){

    companion object {
        private var idCount = AtomicInteger(0)

        val DiffUtil = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(old: Card, new: Card) = old.id == new.id

            override fun areContentsTheSame(old: Card, new: Card) = old == new
        }
    }
}
