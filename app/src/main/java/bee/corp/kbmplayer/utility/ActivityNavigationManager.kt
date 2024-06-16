package bee.corp.kbmplayer.utility

import android.app.Activity
import android.content.Intent
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.view.activities.PlayerActivity
import bee.corp.kbmplayer.view.activities.PlaylistPlayerActivity
import bee.corp.kbmplayer.view.activities.SongSelectionActivity

class ActivityNavigationManager {
    companion object {
        fun startPlayerActivity(
            c: Activity?,
            position: Int
        ) : Intent {
            val intent = Intent(c, PlayerActivity::class.java)
            intent.putExtra(Constants.IntentStringExtrasNames.MUSIC_TAB_DATA_INDEX_EXTRA_NAME, position)
            c?.startActivity(intent)
            return intent
        }
        fun startSongSelectionActivity(c: Activity, a: ArrayList<MusicTabData>) : Intent {
            val intent = Intent(c, SongSelectionActivity::class.java)
            intent.putExtra(Constants.IntentStringExtrasNames.SONGS_ARRAY_EXTRA_NAME, a)
            c.startActivity(intent)
            return intent
        }
        fun startPlaylistPlayerActivity(c: Activity) : Intent {
            val intent = Intent(c, PlaylistPlayerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            c.startActivity(intent)
            return intent
        }
    }
}