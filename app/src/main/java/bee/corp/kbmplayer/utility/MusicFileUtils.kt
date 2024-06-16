package bee.corp.kbmplayer.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import bee.corp.kbmplayer.R

class MusicFileUtils {
    companion object {
        fun getMusicAlbumArt(filePath: String?, c: Context): Bitmap {
            val retriever = MediaMetadataRetriever()
            return try {
                retriever.setDataSource(filePath)
                val art = retriever.embeddedPicture
                retriever.close()
                BitmapFactory.decodeByteArray(art, 0, art?.size ?: -1)
            } catch (e: Exception) {
                retriever.close()
                BitmapFactory.decodeResource(c.resources, R.drawable.music_art_placeholder)
            } finally {
                retriever.close()
            }
        }
    }
}