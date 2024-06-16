package bee.corp.kbmplayer.view.music

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bee.corp.kbmplayer.databinding.MusicTabLayoutBinding
import bee.corp.kbmplayer.model.MusicTabData
import bee.corp.kbmplayer.utility.AdapterConfig

class MusicTabsAdapter(mt: ArrayList<MusicTabData>, config: AdapterConfig) : RecyclerView.Adapter<MusicTabView>() {
    private var adapterConfig: AdapterConfig

    init {
        adapterConfig = config
    }

    var musicTabsList: ArrayList<MusicTabData> = mt
    var originalMusicTabsList: ArrayList<MusicTabData> = mt

    private lateinit var musicItemClickedListener: MusicItemClickedListener
    private lateinit var musicItemViewClickedListener: MusicItemViewClickedListener
    private lateinit var musicItemCheckBoxClickedListener: MusicItemCheckBoxClickedListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicTabView {
        return MusicTabView(
            MusicTabLayoutBinding.inflate(LayoutInflater.from(parent.context)),
            parent.context)
    }

    override fun getItemCount(): Int {
        return musicTabsList.size
    }

    override fun onBindViewHolder(holder: MusicTabView, position: Int) {
        holder.setTitle(musicTabsList[position].title)
        holder.setAuthor(musicTabsList[position].author)
        holder.setDuration(musicTabsList[position].durationString)
        holder.bindData(adapterConfig)
        holder.addClickListener(object: MusicItemClickedBasicListener {
            override fun onBasicClick(position: Int) {
                musicItemClickedListener.onItemClicked(position, musicTabsList[position])
            }
        })
        if(adapterConfig.selecting) {
            holder.addCheckBoxClickListener(object: MusicItemCheckBoxClickedBasicListener {
                override fun onBasicCheckBoxClick(position: Int, isChecked: Boolean) {
                    musicItemCheckBoxClickedListener.onItemCheckBoxClicked(position, musicTabsList[position], isChecked)
                }
            })
        }
        holder.addViewItemClickBasicListener(object: MusicItemViewClickedBasicListener {
            override fun onBasicViewClick(position: Int, viewType: Int, view: View) {
                musicItemViewClickedListener.onViewClicked(musicTabsList[position], viewType, position, view)
            }
        })
    }

    fun filterList(l: ArrayList<MusicTabData>) {
        this.musicTabsList = l
        notifyDataSetChanged()
    }

    fun revertFilteredList() {
        this.musicTabsList = originalMusicTabsList
        notifyDataSetChanged()
    }

    fun addMusicItemViewClickListener(ml: MusicItemViewClickedListener) {
        this.musicItemViewClickedListener = ml
    }

    fun addMusicItemClickListener(ml: MusicItemClickedListener) {
        this.musicItemClickedListener = ml
    }

    fun addMusicItemCheckBoxClickListener(ml: MusicItemCheckBoxClickedListener) {
        this.musicItemCheckBoxClickedListener = ml
    }

    interface MusicItemViewClickedBasicListener {
        fun onBasicViewClick(position: Int, viewType: Int, view: View)
    }

    interface MusicItemClickedBasicListener {
        fun onBasicClick(position: Int)
    }

    interface MusicItemCheckBoxClickedBasicListener {
        fun onBasicCheckBoxClick(position: Int, isChecked: Boolean)
    }

}