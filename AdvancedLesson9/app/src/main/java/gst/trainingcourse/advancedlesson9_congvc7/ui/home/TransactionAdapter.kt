package gst.trainingcourse.advancedlesson9_congvc7.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import gst.trainingcourse.advancedlesson9_congvc7.data.model.Transaction
import gst.trainingcourse.advancedlesson9_congvc7.databinding.ItemRecyclerTransactionBinding
import gst.trainingcourse.advancedlesson9_congvc7.ui.base.ViewBindingViewHolder
import gst.trainingcourse.advancedlesson9_congvc7.ui.home.TransactionAdapter.TransactionVH
import gst.trainingcourse.advancedlesson9_congvc7.util.layoutInflater

val Transaction.Companion.diffUtil by lazy {
    object : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(old: Transaction, new: Transaction): Boolean {
            return old.inVoidNumber == new.inVoidNumber
        }

        override fun areContentsTheSame(old: Transaction, new: Transaction): Boolean {
            return old == new
        }
    }
}

class TransactionAdapter(
    private val swipeListener: ItemSwipedListener,
) : ListAdapter<Transaction, TransactionVH>(Transaction.diffUtil) {
    fun interface ItemSwipedListener {
        fun onSwipe(transactionInVoid: Int)
    }

    class TransactionVH(
        private val swipeListener: ItemSwipedListener,
        viewGroup: ViewGroup,
    ) : ViewBindingViewHolder<Transaction, ItemRecyclerTransactionBinding>(
        ItemRecyclerTransactionBinding.inflate(viewGroup.layoutInflater, viewGroup, false)
    ) {
        override fun onBind(item: Transaction) {
            binding.transaction = item
            binding.executePendingBindings()
        }

        fun onSwipe() {
            cachedItem?.inVoidNumber?.let(swipeListener::onSwipe)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionVH {
        return TransactionVH(swipeListener, parent)
    }

    override fun onBindViewHolder(holder: TransactionVH, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
