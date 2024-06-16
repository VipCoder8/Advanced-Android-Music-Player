package bee.corp.kbmplayer.view.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import bee.corp.kbmplayer.databinding.CreatePlaylistDialogLayoutBinding
import bee.corp.kbmplayer.databinding.EditPlaylistDialogLayoutBinding
import bee.corp.kbmplayer.databinding.FragmentPlaylistsBinding
import bee.corp.kbmplayer.model.PlaylistTabData
import bee.corp.kbmplayer.utility.ActivityNavigationManager
import bee.corp.kbmplayer.utility.Constants
import bee.corp.kbmplayer.view.DialogCreator
import bee.corp.kbmplayer.view.RecyclerViewItemDivider
import bee.corp.kbmplayer.view.playlist.PlaylistItemViewClickedListener
import bee.corp.kbmplayer.view.playlist.PlaylistTabsAdapter
import bee.corp.kbmplayer.viewmodel.playlist.PlaylistManagement
import bee.corp.kbmplayer.viewmodel.playlist.PlaylistSongsManagement
import bee.corp.kbmplayer.viewmodel.playlist.PlaylistsSharing
import bee.corp.kbmplayer.viewmodel.music.SongsSharing

class PlaylistsFragment : Fragment() {
    private var playlistFragmentBinding: FragmentPlaylistsBinding? = null

    private var addPlaylistDialogLayoutBinding: CreatePlaylistDialogLayoutBinding? = null
    private lateinit var addPlaylistDialog: Dialog

    private lateinit var playlistDeletionDialog: Dialog

    private var playlistEditDialogLayoutBinding: EditPlaylistDialogLayoutBinding? = null
    private lateinit var playlistEditDialog: Dialog

    private lateinit var playlistManagement: PlaylistManagement
    private lateinit var songsSharingViewModel: SongsSharing

    private lateinit var playlistTabsAdapter: PlaylistTabsAdapter

    private lateinit var playlistTabViewClickListener: PlaylistItemViewClickedListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        playlistFragmentBinding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        initViews(inflater)
        initViewsListeners()
        initViewModels()
        observeViewModels()
        return playlistFragmentBinding?.root
    }

    private fun initViews(inflater: LayoutInflater) {
        addPlaylistDialogLayoutBinding = CreatePlaylistDialogLayoutBinding.inflate(inflater)
        playlistEditDialogLayoutBinding = EditPlaylistDialogLayoutBinding.inflate(inflater)

        addPlaylistDialog = DialogCreator.createViewDialog(requireActivity(),
            addPlaylistDialogLayoutBinding?.root, Constants.DialogStrings.CREATE_PLAYLIST_DIALOG_MESSAGE,
            Constants.DialogStrings.CREATE_PLAYLIST_DIALOG_TITLE,
            Constants.DialogStrings.ButtonTexts.CREATE_PLAYLIST_DIALOG_POSITIVE_BUTTON_TEXT
        ) { dialog, _ ->
            if(addPlaylistDialogLayoutBinding?.playlistTitleField?.text.toString().isNotBlank()) {
                playlistManagement.addPlaylist(addPlaylistDialogLayoutBinding?.playlistTitleField?.text.toString())
            }
            dialog.dismiss()
        }
    }

    private fun initViewModels() {
        playlistManagement = ViewModelProvider(this)[PlaylistManagement::class.java]
        songsSharingViewModel = ViewModelProvider(requireActivity())[SongsSharing::class.java]
    }

    private fun observeViewModels() {
        SongsSharing.SharedSongs.getSelectedSongsListLiveData.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                addPlaylistDialog.dismiss()
                if(addPlaylistDialogLayoutBinding?.playlistTitleField?.text.toString().isNotBlank()) {
                    playlistManagement.addPlaylist(addPlaylistDialogLayoutBinding?.playlistTitleField?.text.toString(),
                        it)
                }
            }
        }
        PlaylistSongsManagement.SharedPlaylistManagementLiveData.getDeletedSongLiveData.observe(viewLifecycleOwner) {
            if(playlistTabsAdapter.originalPlaylistList.indexOf(it) != -1) {
                playlistTabsAdapter.originalPlaylistList[playlistTabsAdapter.originalPlaylistList.indexOf(it)] =
                    it
                playlistTabsAdapter.notifyItemChanged(playlistTabsAdapter.originalPlaylistList.indexOf(it))
            }
        }

        playlistManagement.getSearchFoundPlaylistLiveData.observe(viewLifecycleOwner) {
            playlistTabsAdapter.filterList(it)
        }
        playlistManagement.getInitPlaylistLiveData.observe(viewLifecycleOwner) {
            playlistTabsAdapter = PlaylistTabsAdapter(it)
            playlistTabsAdapter.addViewClickListener(this.playlistTabViewClickListener)
            playlistFragmentBinding?.playlistsView?.adapter = playlistTabsAdapter
            playlistFragmentBinding?.playlistsView?.addItemDecoration(RecyclerViewItemDivider(Constants.ViewsDefaultValues.RECYCLER_VIEW_ITEM_DIVIDE_DISTANCE))
        }
        playlistManagement.getAddedPlaylistLiveData.observe(viewLifecycleOwner) {
            playlistTabsAdapter.notifyItemChanged(it)
        }
        playlistManagement.getDeletedPlaylistLiveData.observe(viewLifecycleOwner) {
            playlistTabsAdapter.notifyItemRemoved(it)
        }
        playlistManagement.getEditedPlayLiveData.observe(viewLifecycleOwner) {
            playlistTabsAdapter.notifyItemChanged(it)
        }
    }

    private fun deletePlaylistAndInitDeletionDialog(data: PlaylistTabData, viewType: Int, position: Int) {
        playlistDeletionDialog = DialogCreator.createSimpleDialog(requireActivity(),
            "${Constants.DialogStrings.DELETE_PLAYLIST_DIALOG_MESSAGE} ${data.title}?",
            Constants.DialogStrings.DELETE_PLAYLIST_DIALOG_TITLE,
            Constants.DialogStrings.DELETE_PLAYLIST_POSITIVE_BUTTON_TEXT,
            { _, _ ->
                if(viewType == Constants.PlaylistViewTypes.DELETE_PLAYLIST_BUTTON_VIEW_TYPE) {
                    playlistManagement.deletePlaylist(data, position)
                }
            },
            Constants.DialogStrings.DELETE_PLAYLIST_NEGATIVE_BUTTON_TEXT
        ) { dialog, _ -> dialog.dismiss()}
        playlistDeletionDialog.show()
    }

    private fun editPlaylistAndInitEditDialog(p: PlaylistTabData, originalTitle: String, position: Int) {
        playlistEditDialogLayoutBinding?.newPlaylistTitleField?.setText(originalTitle)
        playlistEditDialog = DialogCreator.createViewDialog(requireActivity(),
            playlistEditDialogLayoutBinding?.root,
            Constants.DialogStrings.EDIT_PLAYLIST_DIALOG_MESSAGE,
            Constants.DialogStrings.EDIT_PLAYLIST_DIALOG_TITLE,
            Constants.DialogStrings.EDIT_PLAYLIST_POSITIVE_BUTTON_TEXT)
            { dialog, _ ->
                playlistManagement.editPlaylist(p,
                    playlistEditDialogLayoutBinding?.newPlaylistTitleField?.text.toString(),
                    position)
                dialog.dismiss()
            }
        playlistEditDialog.show()
    }

    private fun initViewsListeners() {
        playlistFragmentBinding?.searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean { return true }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.isNotBlank() == true) {
                    playlistManagement.searchPlaylist(newText, playlistTabsAdapter.originalPlaylistList)
                } else {
                    playlistTabsAdapter.revertFilteredList()
                }
                return true
            }
        })

        playlistFragmentBinding?.addPlaylistButton?.setOnClickListener {
            addPlaylistDialog.show()
        }
        addPlaylistDialogLayoutBinding?.addSongsButton?.setOnClickListener {
            SongsSharing.SharedSongs.getSelectedSongsListLiveData.value?.clear()
            ActivityNavigationManager.startSongSelectionActivity(requireActivity(),
                songsSharingViewModel.getSongsList())
        }
        this.playlistTabViewClickListener = object: PlaylistItemViewClickedListener {
            override fun onViewClicked(data: PlaylistTabData, viewType: Int, position: Int) {
                if(viewType == Constants.PlaylistViewTypes.DELETE_PLAYLIST_BUTTON_VIEW_TYPE) {
                    deletePlaylistAndInitDeletionDialog(data, viewType, position)
                } else if(viewType == Constants.PlaylistViewTypes.EDIT_PLAYLIST_BUTTON_VIEW_TYPE) {
                    editPlaylistAndInitEditDialog(data, data.title, position)
                } else if(viewType == Constants.PlaylistViewTypes.ROOT_VIEW_TYPE) {
                    ActivityNavigationManager.startPlaylistPlayerActivity(requireActivity())
                    PlaylistsSharing.SharedPlaylists.setPlaylist(data)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playlistFragmentBinding = null
        addPlaylistDialogLayoutBinding = null
        playlistEditDialogLayoutBinding = null
    }

}