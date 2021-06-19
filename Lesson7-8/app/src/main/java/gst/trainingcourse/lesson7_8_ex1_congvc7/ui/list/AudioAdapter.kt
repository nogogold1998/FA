package gst.trainingcourse.lesson7_8_ex1_congvc7.ui.list

import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.Audio
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.LocalAudio
import gst.trainingcourse.lesson7_8_ex1_congvc7.databinding.ItemLocalAudioBinding
import gst.trainingcourse.lesson7_8_ex1_congvc7.util.layoutInflater

private const val LOCAL_AUDIO_TYPE = 1

@IntDef(LOCAL_AUDIO_TYPE)
@Target(AnnotationTarget.TYPE, AnnotationTarget.TYPE_PARAMETER)
annotation class AudioViewType

class AudioAdapter(private val clickListener: ItemClickListener<Audio>) :
    ListAdapter<Audio, AudioVH<ViewBinding, out Audio>>(Audio.diffUtils) {

    override fun getItemViewType(position: Int): @AudioViewType Int = when (getItem(position)) {
        is LocalAudio -> LOCAL_AUDIO_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: @AudioViewType Int
    ): AudioVH<ViewBinding, out Audio> {
        return when (viewType) {
            LOCAL_AUDIO_TYPE -> LocalAudioVH(parent, clickListener)
            else -> throw IllegalArgumentException("viewType is invalid! Was $viewType")
        }
    }

    override fun onBindViewHolder(holder: AudioVH<ViewBinding, out Audio>, position: Int) {
        when (holder) {
            is LocalAudioVH -> holder.bind(getItem(position) as LocalAudio)
        }
    }

    fun interface ItemClickListener<in T> {
        fun onClick(item: T)
    }
}

sealed class AudioVH<out B : ViewBinding, I>(
    protected val binding: B
) : RecyclerView.ViewHolder(binding.root) {
    protected var cachedBindItem: I? = null

    fun bind(item: I) {
        cachedBindItem = item
        onBind(item)
    }

    protected open fun onBind(item: I) = Unit
}

private class LocalAudioVH(
    parent: ViewGroup,
    clickListener: AudioAdapter.ItemClickListener<LocalAudio>
) : AudioVH<ItemLocalAudioBinding, LocalAudio>(
    ItemLocalAudioBinding.inflate(parent.layoutInflater, parent, false)
) {
    init {
        binding.root.setOnClickListener { cachedBindItem?.let(clickListener::onClick) }
    }

    override fun onBind(item: LocalAudio) {
        with(binding) {
            audioTitle.text = item.title
            audioArtist.text = item.artist
            audioDuration.text = item.duration.toString()
            imageCover.setImageBitmap(item.coverImage)
        }
    }
}
