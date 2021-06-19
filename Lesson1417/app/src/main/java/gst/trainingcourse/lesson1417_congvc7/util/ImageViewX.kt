package gst.trainingcourse.lesson1417_congvc7.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import gst.trainingcourse.lesson1417_congvc7.R

fun ImageView.load(
    url: String,
    requestBuilder: (RequestBuilder<Drawable>.() -> Unit)? = null
) = post {
    if (context == null) return@post
    Glide.with(context)
        .load(Uri.parse(url))
        .centerCrop()
        .error(R.drawable.ic_baseline_broken_image_24)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply { requestBuilder?.invoke(this) }
        .into(this)
}
