package gst.trainingcourse.lesson7_8_ex1_congvc7.data.local

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import gst.trainingcourse.lesson7_8_ex1_congvc7.data.model.LocalAudio
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield

private val MEDIA_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
private val PROJECTION = arrayOf(MediaStore.Audio.Media._ID)
private const val SELECTION = MediaStore.Audio.Media.IS_MUSIC + " != 0"

class LocalAudioSourceImpl(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) :
    LocalAudioSource {
    /** WARNING: This implementation causes exhaustively massive objects allocation */
    override fun getAllLocalAudios(context: Context): Flow<List<LocalAudio>> {
        val cursor = queryAllLocalAudio(context) ?: return emptyFlow()
        return flow {
            try {
                val localAudios = mutableSetOf<LocalAudio>()
                val retriever = MediaMetadataRetriever()
                do {
                    yield()
                    val mediaId = cursor.getLong(0)
                    retriever.setDataSource(context, LocalAudio.getUriFromId(mediaId))
                    localAudios += extractLocalAudio(retriever, mediaId)
                    yield()
                    emit(localAudios.toList())
                } while (cursor.moveToNext())
            } finally {
                withContext(NonCancellable) {
                    cursor.close()
                }
            }
        }
            .flowOn(dispatcher)
    }

    override fun getAllLocalAudiosAsync(
        context: Context,
        scope: CoroutineScope,
    ): Deferred<List<LocalAudio>> = scope.async(dispatcher) {
        val cursor = queryAllLocalAudio(context) ?: return@async emptyList()
        return@async mutableListOf<LocalAudio>().also { localAudios ->
            try {
                val retriever = MediaMetadataRetriever()
                do {
                    val mediaId = cursor.getLong(0)
                    retriever.setDataSource(context, LocalAudio.getUriFromId(mediaId))
                    localAudios += extractLocalAudio(retriever, mediaId)
                } while (cursor.moveToNext() && coroutineContext.isActive)
            } finally {
                withContext(NonCancellable) {
                    cursor.close()
                }
            }
        }
    }

    private fun queryAllLocalAudio(context: Context): Cursor? {
        val cursor = context.contentResolver.query(
            MEDIA_URI,
            PROJECTION,
            SELECTION,
            null,
            null
        )
        if (cursor?.moveToFirst() == false) cursor.close()
        if (cursor == null || cursor.isClosed) return null
        return cursor
    }

    private fun extractLocalAudio(
        retriever: MediaMetadataRetriever,
        mediaId: Long
    ): LocalAudio {
        val art = retriever.embeddedPicture
        return LocalAudio(
            mediaId,
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!!,
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!!,
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)!!,
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong(),
            if (art != null) BitmapFactory.decodeByteArray(art, 0, art.size) else null
        )
    }
}
