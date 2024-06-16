package bee.corp.kbmplayer.view.music

import bee.corp.kbmplayer.model.MusicTabData

interface MusicItemClickedListener {
    fun onItemClicked(position: Int, data: MusicTabData)
}