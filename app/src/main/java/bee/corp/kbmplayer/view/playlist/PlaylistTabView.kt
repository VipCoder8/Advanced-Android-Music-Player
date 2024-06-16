package bee.corp.kbmplayer.view.playlist

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import bee.corp.kbmplayer.R
import bee.corp.kbmplayer.databinding.PlaylistTabLayoutBinding
import bee.corp.kbmplayer.utility.Constants

@SuppressLint("ClickableViewAccessibility")
class PlaylistTabView(l: PlaylistTabLayoutBinding, c: Context) :
    RecyclerView.ViewHolder(l.root) {
    private val playlistTabLayoutBinding: PlaylistTabLayoutBinding = l
    private var songsCount: Int = 0

    private var scaleUpAnimation: Animation = AnimationUtils.loadAnimation(c, R.anim.scale_up_anim)
    private var scaleDownAnimation: Animation = AnimationUtils.loadAnimation(c, R.anim.scale_down_anim)

    private lateinit var viewClickListener: PlaylistTabsAdapter.PlaylistItemViewClickedBasicListener

    init {
        super.itemView.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                super.itemView.startAnimation(scaleUpAnimation)
            }
            if(event.action == MotionEvent.ACTION_DOWN) {
                super.itemView.startAnimation(scaleDownAnimation)
            }
            viewClickListener.onBasicViewClick(adapterPosition,
                Constants.PlaylistViewTypes.ROOT_VIEW_TYPE)
            true
        }
        playlistTabLayoutBinding.deletePlaylistButton.setOnClickListener {
            viewClickListener.onBasicViewClick(adapterPosition,
                Constants.PlaylistViewTypes.DELETE_PLAYLIST_BUTTON_VIEW_TYPE)
        }
        playlistTabLayoutBinding.editPlaylistButton.setOnClickListener {
            viewClickListener.onBasicViewClick(adapterPosition,
                Constants.PlaylistViewTypes.EDIT_PLAYLIST_BUTTON_VIEW_TYPE)
        }
    }

    fun addViewClickedListener(cl: PlaylistTabsAdapter.PlaylistItemViewClickedBasicListener) {
        this.viewClickListener = cl
    }

    fun setTitle(title: String) {
        playlistTabLayoutBinding.playlistTitleView.text = title
    }
    fun setSongsCount(count: Int) {
        playlistTabLayoutBinding.playlistSongCountView.text = "$count Songs"
        songsCount = count
    }
}