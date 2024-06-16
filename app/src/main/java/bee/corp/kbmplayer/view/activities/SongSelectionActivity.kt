package bee.corp.kbmplayer.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import bee.corp.kbmplayer.databinding.ActivitySongSelectorBinding
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.utility.ActivityNavigationManager
import bee.corp.kbmplayer.utility.AdapterConfig
import bee.corp.kbmplayer.view.music.MusicItemCheckBoxClickedListener
import bee.corp.kbmplayer.view.music.MusicItemClickedListener
import bee.corp.kbmplayer.view.music.MusicTabsAdapter
import bee.corp.kbmplayer.viewmodel.music.SongsSharing

class SongSelectionActivity : FragmentActivity() {
    private lateinit var songSelectionActivity: ActivitySongSelectorBinding

    private val adapterConfig = AdapterConfig(true)
    private lateinit var musicTabsAdapter: MusicTabsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        setContentView(songSelectionActivity.root)
        initViewsListeners()
        observeViewModels()
    }

    private fun initViews() {
        songSelectionActivity = ActivitySongSelectorBinding.inflate(LayoutInflater.from(this))
    }

    private fun observeViewModels() {
        SongsSharing.SharedSongs.getSongsListLiveData.observe(this) {
            musicTabsAdapter = MusicTabsAdapter(it, adapterConfig)
            songSelectionActivity.songsSelectionView.setItemViewCacheSize(musicTabsAdapter.musicTabsList.size)
            songSelectionActivity.songsSelectionView.adapter = musicTabsAdapter
            musicTabsAdapter.addMusicItemClickListener(object: MusicItemClickedListener {
                override fun onItemClicked(position: Int, data: MusicTabData) {
                    ActivityNavigationManager.startPlayerActivity(this@SongSelectionActivity, position)
                }
            })
            musicTabsAdapter.addMusicItemCheckBoxClickListener(object: MusicItemCheckBoxClickedListener {
                override fun onItemCheckBoxClicked(position: Int, data: MusicTabData, isChecked: Boolean) {
                    if(isChecked) {
                        SongsSharing.SharedSongs.addSelectedSong(data)
                    } else {
                        SongsSharing.SharedSongs.removeSelectedSong(data)
                    }
                }
            })
        }
    }

    private fun initViewsListeners() {
        songSelectionActivity.backButton.setOnClickListener {
            finish()
        }
        songSelectionActivity.nextButton.setOnClickListener {
            SongsSharing.SharedSongs.updateSelectedSongsLiveData()
            finish()
        }
    }

}