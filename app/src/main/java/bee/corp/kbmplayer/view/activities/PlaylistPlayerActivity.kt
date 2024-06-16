package bee.corp.kbmplayer.view.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.ViewModelProvider
import bee.corp.kbmplayer.R
import bee.corp.kbmplayer.databinding.ActivityPlaylistPlayerBinding
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.utility.ActivityNavigationManager
import bee.corp.kbmplayer.utility.AdapterConfig
import bee.corp.kbmplayer.utility.Constants
import bee.corp.kbmplayer.view.RecyclerViewItemDivider
import bee.corp.kbmplayer.view.music.MusicItemClickedListener
import bee.corp.kbmplayer.view.music.MusicItemViewClickedListener
import bee.corp.kbmplayer.view.music.MusicTabsAdapter
import bee.corp.kbmplayer.viewmodel.playlist.PlaylistSongsManagement
import bee.corp.kbmplayer.viewmodel.playlist.PlaylistsSharing
import bee.corp.kbmplayer.viewmodel.music.SongsSharing

class PlaylistPlayerActivity : AppCompatActivity() {

    private lateinit var playlistPlayerBinding: ActivityPlaylistPlayerBinding

    private lateinit var musicTabsAdapter: MusicTabsAdapter

    private lateinit var musicItemClickListener: MusicItemClickedListener
    private lateinit var musicItemViewClickListener: MusicItemViewClickedListener
    private lateinit var playlistSongsManagement: PlaylistSongsManagement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews(LayoutInflater.from(this))
        initViewsListeners()
        setContentView(playlistPlayerBinding.root)
        initViewModels()
        observeViewModels()
    }
    private fun initViews(inflater: LayoutInflater) {
        playlistPlayerBinding = ActivityPlaylistPlayerBinding.inflate(inflater)
    }
    private fun initViewModels() {
        playlistSongsManagement = ViewModelProvider(this)[PlaylistSongsManagement::class.java]
    }
    private fun observeViewModels() {
        PlaylistsSharing.SharedPlaylists.getPlaylistLiveData.observe(this) {
            updateViewsData(it.title, it.songs.size)
            initMusicsTabsAdapter(it.songs, AdapterConfig(false, true))
            SongsSharing.SharedPlayerSongs.setSongsList(it.songs)
            playlistSongsManagement.init(it)
        }
        playlistSongsManagement.getDeletedSongLiveData.observe(this) {
            musicTabsAdapter.notifyItemRemoved(it)
            updateViewsData(playlistSongsManagement.getPlaylist.title,
                playlistSongsManagement.getPlaylist.songs.size)
            SongsSharing.SharedPlayerSongs.setSongsList(playlistSongsManagement.getPlaylist.songs)
        }
    }
    private fun initMusicsTabsAdapter(a: ArrayList<MusicTabData>, config: AdapterConfig) {
        musicTabsAdapter = MusicTabsAdapter(a, config)
        playlistPlayerBinding.songsView.addItemDecoration(RecyclerViewItemDivider(Constants
            .ViewsDefaultValues
            .RECYCLER_VIEW_ITEM_DIVIDE_DISTANCE))
        playlistPlayerBinding.songsView.adapter = musicTabsAdapter
        musicTabsAdapter.addMusicItemClickListener(musicItemClickListener)
        musicTabsAdapter.addMusicItemViewClickListener(musicItemViewClickListener)
    }
    private fun initViewsListeners() {
        playlistPlayerBinding.backButton.setOnClickListener {
            finish()
        }

        musicItemClickListener = object: MusicItemClickedListener {
            override fun onItemClicked(position: Int, data: MusicTabData) {
                ActivityNavigationManager.startPlayerActivity(this@PlaylistPlayerActivity, position)
            }
        }
        musicItemViewClickListener = object: MusicItemViewClickedListener {
            override fun onViewClicked(data: MusicTabData, viewType: Int, position: Int, view: View) {
                if(viewType == Constants.MusicViewTypes.MORE_BUTTON_VIEW_TYPE) {
                    val moreMenu = PopupMenu(this@PlaylistPlayerActivity, view)
                    moreMenu.menuInflater.inflate(R.menu.music_item_more_actions_menu,
                        moreMenu.menu)
                    moreMenu.show()
                    moreMenu.menu[0].setOnMenuItemClickListener {
                        playlistSongsManagement.deleteSong(position)
                        true
                    }
                }
            }
        }
    }
    private fun updateViewsData(title: String, songsCount: Int) {
        playlistPlayerBinding.playlistTitleView.text = title
        playlistPlayerBinding.playlistSongsCountView.text = "${songsCount} songs"
    }
}