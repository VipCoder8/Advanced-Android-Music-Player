package bee.corp.kbmplayer.viewmodel.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bee.corp.kbmplayer.model.MusicTabData

class SongsSharing: ViewModel() {
    private var songsList: ArrayList<MusicTabData> = ArrayList()

    //Sharing data across non-host activities and fragments
    object SharedSongs {
        @Volatile private var selectedSongsList: ArrayList<MusicTabData> = ArrayList()
        @Volatile private var _mutableSelectedSongsListLiveData: MutableLiveData<ArrayList<MusicTabData>> = MutableLiveData()
        val getSelectedSongsListLiveData: LiveData<ArrayList<MusicTabData>> get() = _mutableSelectedSongsListLiveData

        fun updateSelectedSongsLiveData() {
            _mutableSelectedSongsListLiveData.value = selectedSongsList
        }
        fun addSelectedSong(m: MusicTabData) {
            selectedSongsList += m
        }
        fun removeSelectedSong(m: MusicTabData) {
            selectedSongsList.remove(m)
        }

        private var _mutableSongsListLiveData: MutableLiveData<ArrayList<MusicTabData>> = MutableLiveData()
        val getSongsListLiveData: LiveData<ArrayList<MusicTabData>> get() = _mutableSongsListLiveData

        fun setSongsList(l: ArrayList<MusicTabData>) {
            _mutableSongsListLiveData.value = l
        }
    }
    object SharedPlayerSongs {
        private var _mutableSongsListLiveData: MutableLiveData<ArrayList<MusicTabData>> = MutableLiveData()
        val getSongsListLiveData: LiveData<ArrayList<MusicTabData>> get() = _mutableSongsListLiveData

        fun setSongsList(l: ArrayList<MusicTabData>) {
            _mutableSongsListLiveData.value = l
        }
    }
    fun saveSongsList(l: ArrayList<MusicTabData>) {
        songsList = l
    }
    fun getSongsList() : ArrayList<MusicTabData> {
        return songsList
    }
}