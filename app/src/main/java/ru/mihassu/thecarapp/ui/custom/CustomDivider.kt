package ru.mihassu.thecarapp.ui.custom

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

class CustomDivider(val divider: Drawable?) : RecyclerView.ItemDecoration() {

//    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//
//        for (i in 0 until parent.childCount) {
//            divider?.setBounds(0, 20, 60, 23)
//            divider?.draw(c)
//        }
//
//    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val left = parent.paddingLeft + 40
        val right = parent.width - parent.paddingRight - 40

        for (i in 0 until parent.childCount) {

            val item = parent.getChildAt(i)
            val top = item.bottom + (item.layoutParams as RecyclerView.LayoutParams).bottomMargin
            val bottom = top + (divider?.intrinsicHeight ?: 0)

            divider?.setBounds(left, top, right, bottom)
            divider?.draw(c)
        }

    }

}