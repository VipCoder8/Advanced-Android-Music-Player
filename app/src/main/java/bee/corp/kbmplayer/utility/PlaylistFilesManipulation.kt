package bee.corp.kbmplayer.utility

import android.content.Context
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.model.PlaylistTabData
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

class PlaylistFilesManipulation {
    companion object {
        fun deletePlaylist(p: String) : Boolean {
            return File(p).delete()
        }
        fun createPlaylist(title: String, c: Context) : String {
            val path: String = generatePlaylistFilePath(c, title)
            if(File(path).createNewFile()) {
                return path
            } else {
                return Constants.ErrorStrings.PLAYLIST_CREATION_ERROR
            }
        }
        fun writePlaylistData(p: PlaylistTabData) {
            val file = File(p.filePath)
            val b = ObjectOutputStream(FileOutputStream(file))
            b.writeObject(p)
            b.close()
        }
        fun editPlaylist(p: PlaylistTabData, c: Context) : String {
            val file = File(p.filePath)
            val newFilePath = generatePlaylistFilePath(c, p.title)
            file.renameTo(File(newFilePath))
            writePlaylistData(p)
            return newFilePath
        }
        fun readPlaylists(c: Context) : Array<out File>? {
            return File(getPathToRootFolderOfPlaylists(c)).listFiles()
        }
        private fun generatePlaylistFilePath(c: Context, title: String) : String {
            return "${c.getExternalFilesDir(null)?.absolutePath}/_${title}_${System.currentTimeMillis()}"
        }
        private fun generatePlaylistFileName(c: Context, title: String) : String {
            return "${title}_${System.currentTimeMillis()}"
        }
        fun getPathToRootFolderOfPlaylists(c: Context) : String {
            return "${c.getExternalFilesDir(null)?.absolutePath}/"
        }
    }
}