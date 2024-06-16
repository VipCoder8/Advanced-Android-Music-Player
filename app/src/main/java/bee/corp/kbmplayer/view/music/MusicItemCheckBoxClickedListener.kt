package bee.corp.kbmplayer.view.music

import bee.corp.kbmplayer.model.MusicTabData

interface MusicItemCheckBoxClickedListener {
    fun onItemCheckBoxClicked(position: Int, data: MusicTabData, isChecked: Boolean)
}