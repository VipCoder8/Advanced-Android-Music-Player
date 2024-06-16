package bee.corp.kbmplayer.utility

import bee.corp.kbmplayer.model.MusicTabData

class SongSearcher {
    private var foundList: ArrayList<MusicTabData> = ArrayList()
    fun searchForIn(m: String, list: ArrayList<MusicTabData>) : ArrayList<MusicTabData> {
        foundList.clear()
        for(i in 0..<list.size) {
            if(list[i].title.contains(m, true)) {
                foundList += list[i]
            }
        }
        return foundList
    }
}