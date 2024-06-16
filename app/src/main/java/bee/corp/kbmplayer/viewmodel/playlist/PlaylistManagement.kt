package bee.corp.kbmplayer.viewmodel.playlist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.model.PlaylistTabData
import bee.corp.kbmplayer.utility.PlaylistFilesManipulation
import bee.corp.kbmplayer.utility.PlaylistSearcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

class PlaylistManagement(a: Application) : AndroidViewModel(a) {
    private var playlists: ArrayList<PlaylistTabData> = ArrayList()
    private var playlistSearcher: PlaylistSearcher = PlaylistSearcher()

    private var _playlistInitLiveData: MutableLiveData<ArrayList<PlaylistTabData>> =
        MutableLiveData<ArrayList<PlaylistTabData>>().apply {
            this.value = playlists
    }
    val getInitPlaylistLiveData: LiveData<ArrayList<PlaylistTabData>> get() = _playlistInitLiveData

    private var _playlistAddedLiveData: MutableLiveData<Int> = MutableLiveData()
    val getAddedPlaylistLiveData: LiveData<Int> get() = _playlistAddedLiveData

    private var _playlistEditedLiveData: MutableLiveData<Int> = MutableLiveData()
    val getEditedPlayLiveData: LiveData<Int> get() = _playlistEditedLiveData

    private var _playlistDeletedLiveData: MutableLiveData<Int> = MutableLiveData()
    val getDeletedPlaylistLiveData: LiveData<Int> get() = _playlistDeletedLiveData

    private var _playlistSearchFoundLiveData: MutableLiveData<ArrayList<PlaylistTabData>> = MutableLiveData()
    val getSearchFoundPlaylistLiveData: LiveData<ArrayList<PlaylistTabData>> get() = _playlistSearchFoundLiveData

    init {
        viewModelScope.launch {
            readPlaylists()
        }
    }

    fun deletePlaylist(playlist: PlaylistTabData, position: Int) {
        playlists.remove(playlist)
        if(PlaylistFilesManipulation.deletePlaylist(playlist.filePath)) {
            _playlistDeletedLiveData.postValue(position)
        }
    }

    fun editPlaylist(p: PlaylistTabData,
                     newTitle: String,
                     position: Int) {
        PlaylistFilesManipulation.deletePlaylist(p.filePath)
        p.title = newTitle
        p.filePath = PlaylistFilesManipulation.editPlaylist(p, getApplication())
        _playlistEditedLiveData.postValue(position)
    }

    private fun addPlaylist(p: PlaylistTabData, saving: Boolean = true) {
        viewModelScope.launch {
            if(saving) {
                savePlaylist(p)
            }
        }
        playlists += p
        _playlistAddedLiveData.postValue(playlists.size-1)
    }

    fun searchPlaylist(title: String, arr: ArrayList<PlaylistTabData>) {
        _playlistSearchFoundLiveData.postValue(playlistSearcher.searchForIn(title, arr))
    }

    fun addPlaylist(title: String, arr: ArrayList<MusicTabData> = ArrayList(), saving: Boolean = true,
                    playlistFilePath: String = "") {
        val playlist = PlaylistTabData(arr, title, playlistFilePath)
        viewModelScope.launch {
            if(saving) {
                savePlaylist(playlist)
            }
        }
        playlists += playlist
        _playlistAddedLiveData.postValue(playlists.size-1)
    }

    private suspend fun savePlaylist(m: PlaylistTabData) {
        withContext(Dispatchers.IO) {
            m.filePath = PlaylistFilesManipulation.createPlaylist(m.title,
                getApplication<Application>().applicationContext)
            PlaylistFilesManipulation.writePlaylistData(m)
        }
    }

    private suspend fun readPlaylists() {
        withContext(Dispatchers.IO) {
            for(file: File
            in PlaylistFilesManipulation.readPlaylists(getApplication<Application>().applicationContext)!!) {
                val bis = ObjectInputStream(FileInputStream(file))
                addPlaylist(bis.readObject() as PlaylistTabData, false)
                bis.close()
            }
        }
    }

}