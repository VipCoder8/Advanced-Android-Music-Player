package bee.corp.kbmplayer.view.activities

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import bee.corp.kbmplayer.R
import bee.corp.kbmplayer.databinding.ActivityPlayerBinding
import bee.corp.kbmplayer.utility.Constants
import bee.corp.kbmplayer.utility.MusicFileUtils
import bee.corp.kbmplayer.viewmodel.music.MusicPlayManagement
import bee.corp.kbmplayer.viewmodel.music.SongsSharing

@SuppressLint("ClickableViewAccessibility")
class PlayerActivity : AppCompatActivity() {
    private lateinit var playerActivityLayoutBinding: ActivityPlayerBinding

    private lateinit var playIconDrawable: Drawable
    private lateinit var pauseIconDrawable: Drawable
    private lateinit var loopIconBackgroundDrawable: Drawable

    private lateinit var musicPlayManagement: MusicPlayManagement

    private var userDraggingProgressBar: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setContentView(playerActivityLayoutBinding.root)
        initDrawables()
        initViewsData()
        updateViewsListeners()
        initViewModels()
        observeViewModels()
    }

    private fun initViews() {
        playerActivityLayoutBinding = ActivityPlayerBinding.inflate(LayoutInflater.from(this))
    }
    private fun initViewModels() {
        musicPlayManagement = ViewModelProvider(this)[MusicPlayManagement::class.java]
    }
    private fun observeViewModels() {
        SongsSharing.SharedPlayerSongs.getSongsListLiveData.observe(this) {
            musicPlayManagement.initSongs(it)
            musicPlayManagement.playSong(intent.getIntExtra(
                Constants.IntentStringExtrasNames.MUSIC_TAB_DATA_INDEX_EXTRA_NAME,
                -1))
        }
        musicPlayManagement.getNextSongLiveData.observe(this) {
            updateViewsData(it.filePath, it.title, it.author)
        }
        musicPlayManagement.getPreviousSongLiveData.observe(this) {
            updateViewsData(it.filePath, it.title, it.author)
        }
        musicPlayManagement.getIsSongPlayingLiveData.observe(this) {
            if(it) {
                playerActivityLayoutBinding.playButton.setImageDrawable(pauseIconDrawable)
            } else {
                playerActivityLayoutBinding.playButton.setImageDrawable(playIconDrawable)
            }
        }
        musicPlayManagement.getInitSongLiveData.observe(this) {
            playerActivityLayoutBinding.musicProgress.max = it.duration.toInt()
            updateViewsData(it.filePath, it.title, it.author)
        }
        musicPlayManagement.getSongProgressLiveData.observe(this) {
            if(!userDraggingProgressBar) {
                playerActivityLayoutBinding.musicProgress.progress = it
            }
        }
        musicPlayManagement.getSongEndLiveData.observe(this) {
            if(it) {
                playerActivityLayoutBinding.musicProgress.progress = 0
                playerActivityLayoutBinding.playButton.setImageDrawable(playIconDrawable)
            }
        }
    }
    private fun updateViewsListeners() {
        playerActivityLayoutBinding.playButton.setOnClickListener {
            if (playerActivityLayoutBinding.playButton.drawable == playIconDrawable) {
                musicPlayManagement.resumeMusic()
            } else if(playerActivityLayoutBinding.playButton.drawable == pauseIconDrawable) {
                musicPlayManagement.pauseMusic()
            }
        }
        playerActivityLayoutBinding.previousSongButton.setOnClickListener {
            musicPlayManagement.playPreviousSong()
        }
        playerActivityLayoutBinding.nextSongButton.setOnClickListener {
            musicPlayManagement.playNextSong()
        }
        playerActivityLayoutBinding.musicProgress.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {}
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                userDraggingProgressBar = true
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                musicPlayManagement.changePositionTo(seekBar?.progress)
                userDraggingProgressBar = false
            }
        })
        playerActivityLayoutBinding.loopSongButton.setOnClickListener {
            if(!musicPlayManagement.isLooped()) {
                playerActivityLayoutBinding.loopSongButton.background = loopIconBackgroundDrawable
                musicPlayManagement.loopSong(true)
            } else {
                playerActivityLayoutBinding.loopSongButton.background = null
                musicPlayManagement.loopSong(false)
            }
        }
    }
    private fun initDrawables() {
        playIconDrawable = playerActivityLayoutBinding.playButton.drawable
        pauseIconDrawable = AppCompatResources.getDrawable(applicationContext,
            R.drawable.activity_player_pause_icon)!!
        loopIconBackgroundDrawable = AppCompatResources.getDrawable(applicationContext,
            R.drawable.loop_button_background)!!
    }
    private fun initViewsData() {
        playerActivityLayoutBinding.albumArtView.setImageBitmap(MusicFileUtils.getMusicAlbumArt(
            intent.getStringExtra(Constants.IntentStringExtrasNames.MUSIC_PATH_EXTRA_NAME),
            applicationContext))
        playerActivityLayoutBinding.playerTitleTextView.text = intent.getStringExtra(Constants.IntentStringExtrasNames.MUSIC_NAME_EXTRA_NAME)
        playerActivityLayoutBinding.playerAuthorTextView.text = intent.getStringExtra(Constants.IntentStringExtrasNames.MUSIC_AUTHOR_EXTRA_NAME)
    }
    private fun updateViewsData(path: String, title: String, author: String) {
        playerActivityLayoutBinding.albumArtView.setImageBitmap(MusicFileUtils.getMusicAlbumArt(path, applicationContext))
        playerActivityLayoutBinding.playerTitleTextView.text = title
        playerActivityLayoutBinding.playerAuthorTextView.text = author
    }

    override fun onDestroy() {
        super.onDestroy()
        musicPlayManagement.stopMusic()
    }

    override fun onStop() {
        super.onStop()
        musicPlayManagement.stopMusic()
    }
}