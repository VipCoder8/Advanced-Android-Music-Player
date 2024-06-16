package bee.corp.kbmplayer.viewmodel.playlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bee.corp.kbmplayer.model.PlaylistTabData
import bee.corp.kbmplayer.utility.PlaylistFilesManipulation

class PlaylistSongsManagement(app: Application) : AndroidViewModel(app) {
    private lateinit var currentPlaylist: PlaylistTabData
    val getPlaylist: PlaylistTabData get() = currentPlaylist

    private var _deletedSongLiveData: MutableLiveData<Int> = MutableLiveData()
    val getDeletedSongLiveData: LiveData<Int> get() = _deletedSongLiveData

    fun init(a: PlaylistTabData) {
        this.currentPlaylist = a
    }

    object SharedPlaylistManagementLiveData {
        var _deletedSongLiveData: MutableLiveData<PlaylistTabData> = MutableLiveData()
        val getDeletedSongLiveData: LiveData<PlaylistTabData> get() = _deletedSongLiveData
    }

    fun deleteSong(position: Int) {
        SharedPlaylistManagementLiveData._deletedSongLiveData.postValue(currentPlaylist)
        currentPlaylist.songs.removeAt(position)
        PlaylistFilesManipulation.writePlaylistData(currentPlaylist)
        _deletedSongLiveData.postValue(position)
    }
}