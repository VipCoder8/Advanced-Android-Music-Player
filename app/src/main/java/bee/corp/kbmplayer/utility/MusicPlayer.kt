package bee.corp.kbmplayer.utility

import android.media.MediaPlayer
import java.io.FileInputStream

class MusicPlayer {
    private var player: MediaPlayer = MediaPlayer()

    fun playMusic(fp: String) {
        val fis = FileInputStream(fp)
        player.setDataSource(fis.fd)
        player.prepare()
        player.start()
        fis.close()
    }
    fun isLooping() : Boolean {
        return player.isLooping
    }
    fun seekTo(pos: Int) {
        player.seekTo(pos)
    }
    fun resumePlay() {
        player.start()
    }
    fun getCurrentPosition() : Int {
        return player.currentPosition
    }
    fun pauseCurrentPlay() {
        player.pause()
    }
    fun stopCurrentPlay() {
        player.stop()
        player.reset()
    }
    fun setLooped(b: Boolean) {
        player.isLooping = b
    }
    fun addOnEndListener(v: OnPlaybackEndListener) {
        player.setOnCompletionListener {
            v.onEnd()
        }
    }

    interface OnPlaybackEndListener {
        fun onEnd()
    }

}