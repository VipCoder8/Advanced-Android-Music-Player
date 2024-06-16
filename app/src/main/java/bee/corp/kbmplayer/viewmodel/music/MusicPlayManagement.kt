package bee.corp.kbmplayer.viewmodel.music

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.utility.MusicPlayer

class MusicPlayManagement(a: Application) : AndroidViewModel(a) {
    private var songList: ArrayList<MusicTabData> = ArrayList()

    private var musicPlayer: MusicPlayer = MusicPlayer()

    private var _songInitLiveData: MutableLiveData<MusicTabData> = MutableLiveData()
    val getInitSongLiveData: LiveData<MusicTabData> get() = _songInitLiveData

    private var _nextSongLiveData: MutableLiveData<MusicTabData> = MutableLiveData()
    val getNextSongLiveData: LiveData<MusicTabData> get() = _nextSongLiveData

    private var _previousSongLiveData: MutableLiveData<MusicTabData> = MutableLiveData()
    val getPreviousSongLiveData: LiveData<MusicTabData> get() = _previousSongLiveData

    private var _songPlayingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val getIsSongPlayingLiveData: LiveData<Boolean> get() = _songPlayingLiveData

    private var _songProgressLiveData: MutableLiveData<Int> = MutableLiveData()
    val getSongProgressLiveData: LiveData<Int> get() = _songProgressLiveData

    private var _songEndLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val getSongEndLiveData: LiveData<Boolean> get() = _songEndLiveData

    private var trackingProgressThread: Thread = Thread {
        trackProgress()
    }

    private var selectedSongIndex: Int = 0
    private var isPlaying: Boolean = false
    private var isTrackingProgress: Boolean = false

    init {
        trackingProgressThread.start()
        musicPlayer.addOnEndListener(object: MusicPlayer.OnPlaybackEndListener {
            override fun onEnd() {
                _songEndLiveData.postValue(true)
            }
        })
    }

    fun initSongs(m: ArrayList<MusicTabData>) {
        songList = m
    }

    fun playSong(m: MusicTabData) {
        if(isPlaying) {
            musicPlayer.stopCurrentPlay()
        }
        musicPlayer.playMusic(m.filePath)
        selectedSongIndex = songList.indexOf(m)
        _songInitLiveData.value = m
        _songPlayingLiveData.value = true
        isPlaying = true
        startTrackingProgress()
    }

    fun playSong(m: Int) {
        if(isPlaying) {
            musicPlayer.stopCurrentPlay()
        }
        musicPlayer.playMusic(songList[m].filePath)
        selectedSongIndex = m
        _songInitLiveData.value = songList[m]
        _songPlayingLiveData.value = true
        isPlaying = true
        startTrackingProgress()
    }

    fun stopMusic() {
        musicPlayer.stopCurrentPlay()
        _songPlayingLiveData.value = false
        isPlaying = false
        stopTrackingProgress()
    }
    fun pauseMusic() {
        musicPlayer.pauseCurrentPlay()
        _songPlayingLiveData.value = false
        isPlaying = false
        stopTrackingProgress()
    }
    fun resumeMusic() {
        musicPlayer.resumePlay()
        _songPlayingLiveData.value = true
        isPlaying = true
        startTrackingProgress()
    }
    fun loopSong(b: Boolean) {
        musicPlayer.setLooped(b)
    }
    fun isLooped() : Boolean {
        return musicPlayer.isLooping()
    }
    fun playPreviousSong() {
        stopMusic()
        if(selectedSongIndex - 1 >= 0) {
            selectedSongIndex--
            playSong(songList[selectedSongIndex])
            _previousSongLiveData.value = songList[selectedSongIndex]
        } else {
            selectedSongIndex = songList.size-1
            playSong(songList[selectedSongIndex])
            _previousSongLiveData.value = songList[selectedSongIndex]
        }
    }
    fun playNextSong() {
        stopMusic()
        if(selectedSongIndex + 1 < songList.size) {
            selectedSongIndex++
            playSong(songList[selectedSongIndex])
            _nextSongLiveData.value = songList[selectedSongIndex]
        } else {
            selectedSongIndex = 0
            playSong(songList[selectedSongIndex])
            _nextSongLiveData.value = songList[selectedSongIndex]
        }
    }

    fun changePositionTo(pos: Int?) {
        musicPlayer.seekTo(pos!!)
    }

    private fun startTrackingProgress() {
        isTrackingProgress = true
    }
    private fun stopTrackingProgress() {
        isTrackingProgress = false
    }

    private fun trackProgress() {
        while(true) {
            if(isTrackingProgress) {
                _songProgressLiveData.postValue(musicPlayer.getCurrentPosition())
                Thread.sleep(100)
            }
        }
    }

}