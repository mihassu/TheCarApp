package ru.mihassu.thecarapp.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.mihassu.thecarapp.ui.ItemTouchAdapter

class ItemTouchCallback(private val adapter: ItemTouchAdapter): ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val swipeFlags: Int = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        adapter.onItemDismiss(viewHolder.absoluteAdapterPosition)
    }

    //Чтобы поддерживалось смахивание
    override fun isItemViewSwipeEnabled() = true

    //Чтобы поддерживалось перетаскивание при длительном касании
    override fun isLongPressDragEnabled() = true

}