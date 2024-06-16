package bee.corp.kbmplayer.view.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bee.corp.kbmplayer.databinding.PlaylistTabLayoutBinding
import bee.corp.kbmplayer.model.PlaylistTabData

class PlaylistTabsAdapter(l: ArrayList<PlaylistTabData>) : RecyclerView.Adapter<PlaylistTabView>(){
    private var playlistList: ArrayList<PlaylistTabData> = l
    var originalPlaylistList: ArrayList<PlaylistTabData> = l

    private lateinit var viewClickListener: PlaylistItemViewClickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTabView {
        return PlaylistTabView(PlaylistTabLayoutBinding.inflate(LayoutInflater.from(parent.context)), parent.context)
    }

    override fun getItemCount(): Int {
        return playlistList.size
    }

    override fun onBindViewHolder(holder: PlaylistTabView, position: Int) {
        holder.setTitle(playlistList[position].title)
        holder.setSongsCount(playlistList[position].songs.size)
        holder.addViewClickedListener(object: PlaylistItemViewClickedBasicListener {
            override fun onBasicViewClick(position: Int, viewType: Int) {
                this@PlaylistTabsAdapter.viewClickListener.onViewClicked(originalPlaylistList[position],
                    viewType,
                    position)
            }
        })
    }

    fun filterList(l: ArrayList<PlaylistTabData>) {
        this.playlistList = l
        notifyDataSetChanged()
    }

    fun revertFilteredList() {
        this.playlistList = originalPlaylistList
        notifyDataSetChanged()
    }

    fun addViewClickListener(cl: PlaylistItemViewClickedListener) {
        this.viewClickListener = cl
    }

    interface PlaylistItemViewClickedBasicListener {
        fun onBasicViewClick(position: Int, viewType: Int)
    }

}