package bee.corp.kbmplayer.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewItemDivider(ds: Int) : RecyclerView.ItemDecoration() {
    private val divideSpace: Int = ds
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = divideSpace
    }
}