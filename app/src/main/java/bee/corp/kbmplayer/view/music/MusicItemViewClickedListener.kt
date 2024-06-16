package bee.corp.kbmplayer.view.music

import android.view.View
import bee.corp.kbmplayer.model.MusicTabData

interface MusicItemViewClickedListener {
    fun onViewClicked(data: MusicTabData, viewType: Int, position: Int, view: View)
}