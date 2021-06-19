package gst.trainingcourse.lesson9_ex1_congvc7.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.lesson9_ex1_congvc7.data.Contact
import gst.trainingcourse.lesson9_ex1_congvc7.databinding.ItemContactBinding

class ContactAdapter : ListAdapter<Contact, ContactAdapter.ContactVH>(
    object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactVH {
        return ContactVH(parent)
    }

    override fun onBindViewHolder(holder: ContactVH, position: Int) {
        holder.bind(getItem(position))
    }

    private var cachedList: Any? = null
    override fun submitList(list: List<Contact>?, commitCallback: Runnable?) {
        if (cachedList === list) {
            notifyDataSetChanged()
        } else {
            super.submitList(list, commitCallback)
        }
        cachedList = list
    }

    override fun submitList(list: List<Contact>?) {
        submitList(list, null)
    }

    class ContactVH
    private constructor(private val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {

        constructor(root: ViewGroup) : this(
            ItemContactBinding.inflate(root.layoutInflater(), root, false)
        )

        fun bind(contact: Contact) {
            binding.contactDisplayNameTv.text = contact.displayName
            binding.phoneNumberTv.text = contact.phoneNumber
        }
    }
}

private fun ViewGroup.layoutInflater() = LayoutInflater.from(context)
