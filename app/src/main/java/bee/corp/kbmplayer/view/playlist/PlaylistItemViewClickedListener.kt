package bee.corp.kbmplayer.view.playlist

import bee.corp.kbmplayer.model.PlaylistTabData

interface PlaylistItemViewClickedListener {
    fun onViewClicked(data: PlaylistTabData, viewType: Int, position: Int)
}