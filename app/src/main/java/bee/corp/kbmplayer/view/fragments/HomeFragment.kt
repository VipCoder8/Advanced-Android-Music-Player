package bee.corp.kbmplayer.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import bee.corp.kbmplayer.R
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.utility.ActivityNavigationManager
import bee.corp.kbmplayer.utility.AdapterConfig
import bee.corp.kbmplayer.utility.Constants
import bee.corp.kbmplayer.view.music.MusicItemClickedListener
import bee.corp.kbmplayer.view.RecyclerViewItemDivider
import bee.corp.kbmplayer.view.music.MusicTabsAdapter
import bee.corp.kbmplayer.viewmodel.music.MusicLoader
import bee.corp.kbmplayer.viewmodel.music.SongsSharing

class HomeFragment : Fragment() {
    private lateinit var musicsView: RecyclerView
    private lateinit var searchView: SearchView

    private val adapterConfig = AdapterConfig(false)
    private lateinit var musicsAdapter: MusicTabsAdapter
    private lateinit var musicLoader: MusicLoader
    private lateinit var songsSharingViewModel: SongsSharing
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewGroup: ViewGroup = inflater.inflate(R.layout.fragment_home, container, false) as ViewGroup
        initViews(viewGroup)
        initViewModels()
        observeViewModels()
        initViewsListeners()
        return viewGroup
    }
    private fun initViews(v: ViewGroup) {
        musicsView = v.findViewById(R.id.musics_view)
        searchView = v.findViewById(R.id.search_view)
    }
    private fun initViewModels() {
        musicLoader = ViewModelProvider(this)[MusicLoader::class.java]
        musicLoader.loadMusicFiles()
        songsSharingViewModel = ViewModelProvider(requireActivity())[SongsSharing::class.java]
    }
    private fun initViewsListeners() {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.isNotBlank() == true) {
                    searchSongs(newText)
                } else {
                    musicsAdapter.revertFilteredList()
                }
                return true
            }
        })
    }
    private fun searchSongs(title: String) {
        musicLoader.searchSongs(title, musicsAdapter.originalMusicTabsList)
    }
    private fun observeViewModels() {
        musicLoader.mutableMusicsLiveData.observe(viewLifecycleOwner) {
            musicsAdapter = MusicTabsAdapter(it, adapterConfig)
            musicsView.addItemDecoration(RecyclerViewItemDivider(Constants.ViewsDefaultValues.RECYCLER_VIEW_ITEM_DIVIDE_DISTANCE))
            musicsView.setItemViewCacheSize(it.size)
            musicsView.adapter = musicsAdapter
            musicsAdapter.addMusicItemClickListener(object: MusicItemClickedListener {
                override fun onItemClicked(position: Int, data: MusicTabData) {
                    ActivityNavigationManager.startPlayerActivity(requireActivity(),
                        musicsAdapter.originalMusicTabsList.indexOf(data))
                }
            })
            SongsSharing.SharedSongs.setSongsList(it)
            SongsSharing.SharedPlayerSongs.setSongsList(it)
            songsSharingViewModel.saveSongsList(it)
        }
        musicLoader.mutableSongsFoundLiveData.observe(viewLifecycleOwner) {
            musicsAdapter.filterList(it)
        }
    }
}