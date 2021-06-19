package gst.trainingcourse.lesson7_8_ex1_congvc7.data.model

import android.content.ContentUris
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.recyclerview.widget.DiffUtil

sealed class Audio {
    abstract val id: Long
    abstract val mediaUri: Uri

    companion object {
        val diffUtils = object : DiffUtil.ItemCallback<Audio>() {
            override fun areItemsTheSame(oldItem: Audio, newItem: Audio): Boolean {
                return when {
                    oldItem is LocalAudio && newItem is LocalAudio -> oldItem.id == newItem.id
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: Audio, newItem: Audio): Boolean {
                return oldItem == newItem
            }
        }
    }
}

data class LocalAudio(
    override val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val duration: Long,
    val coverImage: Bitmap? = null
) : Audio() {
    override val mediaUri: Uri by lazy { getUriFromId(id) }

    companion object {
        fun getUriFromId(mediaId: Long): Uri =
            ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, mediaId)
        // Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString() + "/" + mediaId)
    }
}
