package ru.mihassu.thecarapp.ui.notedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_note_details.*
import ru.mihassu.thecarapp.R
import ru.mihassu.thecarapp.data.entity.SparePart
import ru.mihassu.thecarapp.data.entity.SparePartsNote
import ru.mihassu.thecarapp.ui.FragmentCallback
import ru.mihassu.thecarapp.ui.ItemTouchCallback
import ru.mihassu.thecarapp.ui.custom.CustomDivider

class NoteDetailsVPAdapter(
    var dataList: MutableList<SparePartsNote> = mutableListOf(),
    val fragmentCallBack: (note: FragmentCallback) -> Unit)
    : RecyclerView.Adapter<NoteDetailsVPAdapter.DetailsViewHolder>() {


    fun addDetails(id: String, details: SparePart) : Int? {
        for (i in 0 until dataList.size) {
            if (dataList[i].id == id) {
                dataList[i].detailsList.add(details)
                return i
            }
        }
        return null
    }

    fun removeDetails(id: String, details: SparePart) : Int? {
        for (i in 0 until dataList.size) {
            if (dataList[i].id == id) {
                for (j in 0 until dataList[i].detailsList.size) {
                    if (dataList[i].detailsList[j].id == details.id) {
                        dataList[i].detailsList.removeAt(j)
                        return i
                    }
                }
            }
        }
        return null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.fragment_note_details, parent, false)
        return DetailsViewHolder(v)
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class DetailsViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(position: Int) {
            tv_sp_date.text = dataList[position].date
            tv_sp_mileage.text = dataList[position].mileage.toString()
            initRecyclerView(position)
        }

        private fun initRecyclerView(pos: Int) {
            val rvAdapter = NoteDetailsRVAdapter{editedData ->
                when (editedData) {
                    is FragmentCallback.OnNoteEdited<*> -> {
                        fragmentCallBack.invoke(FragmentCallback.OnNoteEdited(dataList[pos].id to editedData.data as SparePart))
//                        notifyItemChanged(pos)
                    }
                    is FragmentCallback.OnNoteDeleted<*> -> {
                        fragmentCallBack.invoke(FragmentCallback.OnNoteDeleted(dataList[pos].id to editedData.data as SparePart))
//                        notifyItemChanged(pos)
                    }
                }
            }

            rv_details.apply {
                layoutManager = LinearLayoutManager(containerView.context)
                adapter = rvAdapter
                addItemDecoration(CustomDivider(ContextCompat.getDrawable(containerView.context, R.drawable.items_divider)))
                val itemTouchCallback = ItemTouchCallback(rvAdapter)
                val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
                itemTouchHelper.attachToRecyclerView(this)
            }

            rvAdapter.dataList = dataList[pos].detailsList
            rvAdapter.notifyDataSetChanged()
        }
    }
}