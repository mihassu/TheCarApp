package ru.mihassu.thecarapp.ui.notedetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_note_details.*
import kotlinx.android.synthetic.main.card_note_details_edit.*
import ru.mihassu.thecarapp.R
import ru.mihassu.thecarapp.data.entity.SparePart
import ru.mihassu.thecarapp.ui.FragmentCallback
import ru.mihassu.thecarapp.ui.ItemTouchAdapter
import ru.mihassu.thecarapp.ui.base.BaseViewHolder

class NoteDetailsRVAdapter(var dataList: MutableList<SparePart> = mutableListOf(),
                           val onDataChanged: (FragmentCallback) -> Unit)
    : RecyclerView.Adapter<BaseViewHolder>(), ItemTouchAdapter {

    companion object {
        const val TEXT_LAYOUT = 0
        const val EDITABLE_LAYOUT = 1
        const val NO_EDIT = -1
    }

    var currentEditPos = NO_EDIT


    override fun getItemViewType(position: Int): Int =
        when (position) {
            currentEditPos -> EDITABLE_LAYOUT
            else -> TEXT_LAYOUT
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        when (viewType) {
            EDITABLE_LAYOUT -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.card_note_details_edit, parent, false)
                ViewHolderEdit(v)
            }
            else -> {
                val v = LayoutInflater.from(parent.context).inflate(R.layout.card_note_details, parent, false)
                ViewHolderText(v)
            }
        }


    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ViewHolderText(override val containerView: View) : BaseViewHolder(containerView), LayoutContainer {

        override fun bind(position: Int) {

            tv_part_name.text = dataList[position].partName
            tv_comments.text = dataList[position].comment

            // сменить viewType на редактируемый
            tv_edit_button.setOnClickListener {
                currentEditPos = position
                notifyItemChanged(position)
            }
        }
    }

    inner class ViewHolderEdit(override val containerView: View) : BaseViewHolder(containerView), LayoutContainer {

        override fun bind(position: Int) {
            et_part_name.setText(dataList[position].partName)
            et_comments.setText(dataList[position].comment)

            //применить изменения
            tv_apply_button_edit.setOnClickListener {
                currentEditPos = NO_EDIT
                dataList[position].partName = et_part_name.text.toString()
                dataList[position].comment = et_comments.text.toString()
                onDataChanged.invoke(FragmentCallback.OnNoteEdited(dataList[position]))
                notifyItemChanged(position)
            }
        }
    }

    override fun onItemDismiss(position: Int) {
        val deleted = dataList[position].copy()
//        dataList.removeAt(position)
        onDataChanged.invoke(FragmentCallback.OnNoteDeleted(deleted))
//        notifyItemRemoved(position)
    }
}
