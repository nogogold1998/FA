package gst.trainingcourse.lesson4_congvc7

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val itemClickListener: ItemClickListener) :
    ListAdapter<User, UserAdapter.UserVH>(User.DiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserVH(itemClickListener, parent)

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        getItem(position).let(holder::bind)
    }

    class UserVH private constructor(
        itemClickListener: ItemClickListener,
        view: View,
    ) : RecyclerView.ViewHolder(view) {
        companion object {
            @get:LayoutRes
            private val layoutId: Int
                get() = R.layout.item_user
        }

        private val usernameTextView = view.findViewById<TextView>(R.id.textUsername)
        private val firstnameTextView = view.findViewById<TextView>(R.id.textFirstname)
        private val lastnameTextView = view.findViewById<TextView>(R.id.textLastname)
        private val ageTextView = view.findViewById<TextView>(R.id.textAge)

        private var cachedUser: User? = null

        init {
            view.findViewById<Button>(R.id.buttonViewDetails).setOnClickListener {
                cachedUser?.let(itemClickListener::onClick)
            }
        }

        constructor(
            itemClickListener: ItemClickListener,
            parent: ViewGroup,
            attachToRoot: Boolean = false,
        ) : this(
            itemClickListener,
            LayoutInflater.from(parent.context).inflate(layoutId, parent, attachToRoot),
        )

        fun bind(user: User) {
            cachedUser = user
            usernameTextView.text = user.username
            firstnameTextView.text = user.firstname
            lastnameTextView.text = user.lastname
            ageTextView.text = user.age.toString()
        }
    }

    fun interface ItemClickListener {
        fun onClick(user: User)
    }
}
