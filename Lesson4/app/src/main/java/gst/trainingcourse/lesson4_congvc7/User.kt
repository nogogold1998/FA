package gst.trainingcourse.lesson4_congvc7

import androidx.recyclerview.widget.DiffUtil

data class User(val username: String, val firstname: String, val lastname: String, val age: Int) {
    companion object {
        val DiffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(old: User, new: User) =
                old.username == new.username

            override fun areContentsTheSame(old: User, new: User) = old == new
        }
    }
}
