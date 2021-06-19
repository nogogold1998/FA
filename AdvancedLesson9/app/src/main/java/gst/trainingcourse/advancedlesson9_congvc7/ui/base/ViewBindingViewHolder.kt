package gst.trainingcourse.advancedlesson9_congvc7.ui.base

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class ViewBindingViewHolder<Item, out Binding : ViewBinding>(
    protected val binding: Binding
) : RecyclerView.ViewHolder(binding.root) {

    protected var cachedItem: Item? = null

    @CallSuper
    open fun bind(item: Item) {
        onBind(item)
        cachedItem = item
    }

    protected abstract fun onBind(item: Item)
}
