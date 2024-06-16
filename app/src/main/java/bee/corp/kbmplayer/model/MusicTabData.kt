package bee.corp.kbmplayer.model

import java.io.Serializable

data class MusicTabData(var title: String,
                        var author: String,
                        var durationString: String,
                        var filePath: String,
                        var duration: Long) : Serializable
