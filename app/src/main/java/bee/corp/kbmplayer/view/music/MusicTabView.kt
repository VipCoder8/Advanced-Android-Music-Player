package bee.corp.kbmplayer.view.music

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import bee.corp.kbmplayer.R
import bee.corp.kbmplayer.databinding.MusicTabLayoutBinding
import bee.corp.kbmplayer.utility.AdapterConfig
import bee.corp.kbmplayer.utility.Constants

@SuppressLint("ClickableViewAccessibility")
class MusicTabView(m: MusicTabLayoutBinding, c: Context) : RecyclerView.ViewHolder(m.root) {
    private var musicTabLayoutBinding = m

    private var scaleUpAnimation: Animation = AnimationUtils.loadAnimation(c, R.anim.scale_up_anim)
    private var scaleDownAnimation: Animation = AnimationUtils.loadAnimation(c, R.anim.scale_down_anim)

    private lateinit var clickListener: MusicTabsAdapter.MusicItemClickedBasicListener
    private lateinit var checkBoxClickListener: MusicTabsAdapter.MusicItemCheckBoxClickedBasicListener
    private lateinit var viewClickListener: MusicTabsAdapter.MusicItemViewClickedBasicListener

    init {
        super.itemView.setOnTouchListener { _, event ->
            if(event.action == MotionEvent.ACTION_UP) {
                clickListener.onBasicClick(adapterPosition)
            }
            if(event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                super.itemView.startAnimation(scaleUpAnimation)
            }
            if(event.action == MotionEvent.ACTION_DOWN) {
                super.itemView.startAnimation(scaleDownAnimation)
            }
            true
        }
        musicTabLayoutBinding.selectionCheckBox.setOnCheckedChangeListener { _, isChecked ->
            this.checkBoxClickListener.onBasicCheckBoxClick(adapterPosition, isChecked)
        }
        musicTabLayoutBinding.moreButton.setOnClickListener {
            viewClickListener.onBasicViewClick(adapterPosition, Constants.MusicViewTypes.MORE_BUTTON_VIEW_TYPE, musicTabLayoutBinding.moreButton)
        }
    }

    fun bindData(config: AdapterConfig) {
        musicTabLayoutBinding.config = config
    }

    fun addViewItemClickBasicListener(cl: MusicTabsAdapter.MusicItemViewClickedBasicListener) {
        this.viewClickListener = cl
    }

    fun addClickListener(cl: MusicTabsAdapter.MusicItemClickedBasicListener) {
        this.clickListener = cl
    }

    fun addCheckBoxClickListener(cl: MusicTabsAdapter.MusicItemCheckBoxClickedBasicListener) {
        this.checkBoxClickListener = cl
    }

    fun setTitle(title: String) {
        musicTabLayoutBinding.titleTextView.text = title
    }
    fun setDuration(duration: String) {
        musicTabLayoutBinding.durationTextView.text = duration
    }
    fun setAuthor(author: String) {
        musicTabLayoutBinding.authorTextView.text = author
    }

}