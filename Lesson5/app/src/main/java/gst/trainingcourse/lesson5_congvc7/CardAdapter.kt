package gst.trainingcourse.lesson5_congvc7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class Adapter(private val itemClickListener: (Card) -> Unit)
class CardAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<Card, CardAdapter.CardVH>(Card.DiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CardVH(parent, itemClickListener)

    override fun onBindViewHolder(holder: CardVH, position: Int) {
        getItem(position).let(holder::bind)
    }

    class CardVH private constructor(
        view: View,
        clickListener: ItemClickListener,
    ) : RecyclerView.ViewHolder(view) {
        private val textHolderName: TextView = view.findViewById(R.id.card_text_holder_name)

        private var cachedCard: Card? = null

        init {
            view.rootView.setOnClickListener { cachedCard?.let(clickListener::onClick) }
        }

        constructor(
            parent: ViewGroup,
            itemClickListener: ItemClickListener,
            attachToRoot: Boolean = false
        ) : this(
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, attachToRoot),
            itemClickListener
        )

        fun bind(card: Card) {
            cachedCard = card
            textHolderName.text = card.holderName
        }
    }

    fun interface ItemClickListener {
        fun onClick(item: Card)
    }
}
