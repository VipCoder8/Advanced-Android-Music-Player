package bee.corp.kbmplayer.utility

import bee.corp.kbmplayer.model.PlaylistTabData

class PlaylistSearcher {
    private var foundList: ArrayList<PlaylistTabData> = ArrayList()
    fun searchForIn(m: String, list: ArrayList<PlaylistTabData>) : ArrayList<PlaylistTabData> {
        foundList.clear()
        for(i in 0..<list.size) {
            if(list[i].title.contains(m, true)) {
                foundList += list[i]
            }
        }
        return foundList
    }
}