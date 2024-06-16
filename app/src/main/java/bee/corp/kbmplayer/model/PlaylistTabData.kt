package bee.corp.kbmplayer.model

import java.io.Serializable

data class PlaylistTabData(var songs: ArrayList<MusicTabData>, var title: String, var filePath: String) : Serializable
