package ru.mihassu.thecarapp.ui.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_notes.view.*
import ru.mihassu.thecarapp.R
import ru.mihassu.thecarapp.data.entity.SparePartsNote
import ru.mihassu.thecarapp.data.entity.compare
import ru.mihassu.thecarapp.ui.FragmentCallback
import ru.mihassu.thecarapp.ui.ItemTouchAdapter

class NotesRVAdapter(
    var dataList: MutableList<SparePartsNote> = mutableListOf(),
    var onNoteClickListener: OnNoteClickListener? = null,
    private val fragmentCallback: (FragmentCallback) -> Unit
) : RecyclerView.Adapter<NotesRVAdapter.ViewHolder>(), ItemTouchAdapter {

    //добавить новую запись с учетом сортировки по дате и вернуть позицию
    fun addNewItem(note: SparePartsNote) : Int {
        for (i in 0 until dataList.size) {
            if (dataList[i].compare(note) == 1) {
                dataList.add(i, note)
                return i
            }
        }
        dataList.add(note)
        return dataList.size
    }

    //удалить запись и вернуть позицию
    fun deleteItem(note: SparePartsNote) : Int? {
        for (i in 0 until dataList.size) {
            if (dataList[i] == note) {
                dataList.removeAt(i)
                return i
            }
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_notes, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(pos: Int) {
            with(itemView) {
                tv_sp_date.text = dataList[pos].date
                tv_sp_mileage.text = dataList[pos].mileage.toString()
                var parts = buildString {
                    for (sp in dataList[pos].detailsList) {
                        append(sp.partName).append(", ")
                    }
                }
                et_part.text = parts
                setOnClickListener{onNoteClickListener?.onNoteClick(pos)}
            }
        }
    }

    interface OnNoteClickListener {
        fun onNoteClick(position: Int)
    }

    //срабатывает при свайпе элемента
    override fun onItemDismiss(position: Int) {
        fragmentCallback.invoke(FragmentCallback.OnNoteDeleted(dataList[position]))
    }
}