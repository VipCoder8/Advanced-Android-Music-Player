package bee.corp.kbmplayer.viewmodel.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bee.corp.kbmplayer.model.PlaylistTabData

class PlaylistsSharing : ViewModel() {
    object SharedPlaylists {
        private var _playlistsLiveData: MutableLiveData<PlaylistTabData> = MutableLiveData()
        val getPlaylistLiveData: LiveData<PlaylistTabData> get() = _playlistsLiveData

        fun setPlaylist(p: PlaylistTabData) {
            _playlistsLiveData.postValue(p)
        }
    }
}