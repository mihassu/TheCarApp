package ru.mihassu.thecarapp.ui.notedetails

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_note_details_vp.*
import ru.mihassu.thecarapp.App
import ru.mihassu.thecarapp.R
import ru.mihassu.thecarapp.data.db.FireStoreProvider
import ru.mihassu.thecarapp.data.entity.SparePart
import ru.mihassu.thecarapp.data.entity.SparePartsNote
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.repository.NotesRepositoryImpl
import ru.mihassu.thecarapp.repository.RequestResult
import ru.mihassu.thecarapp.repository.SparePartsRepositoryImpl
import ru.mihassu.thecarapp.ui.FragmentActionListener
import ru.mihassu.thecarapp.ui.FragmentCallback
import ru.mihassu.thecarapp.ui.ViewModelFactory
import ru.mihassu.thecarapp.ui.notes.NotesFragment
import ru.mihassu.thecarapp.util.notesComparator
import ru.mihassu.thecarapp.util.showKeyboard
import java.util.*
import javax.inject.Inject

class NoteDetailsFragmentVP : Fragment() {

    private lateinit var mainActivity: FragmentActionListener
    private var position: Int? = 0
    private var holderPosition: Int = 0
    private lateinit var vpAdapter: NoteDetailsVPAdapter
    private lateinit var navController: NavController
    private lateinit var dialogView: View
    private lateinit var etNewPartName: EditText
    private lateinit var etNewComment: EditText
    private lateinit var newDetailsDialog: AlertDialog
    @Inject
    lateinit var repository : INotesRepository

    private val viewModel: NoteDetailsViewModelVP by lazy {
        ViewModelProvider(this, ViewModelFactory(repository)).get(NoteDetailsViewModelVP::class.java)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as FragmentActionListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d { "NoteDetailsFragment - onCreate()" }
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).getAppComponent().inject(this)
        position = arguments?.getInt(NotesFragment.POSITION_EXTRA)
        viewModel.getNotesLiveData().observe(this, Observer { result ->
            when (result) {
                is RequestResult.SuccessLoad<*> -> showData(result.data as MutableList<SparePartsNote>)
                is RequestResult.SuccessAdd<*> -> {
                    val added = result.data as Pair<String, SparePart>
                    showToastOnResult("Добавлено: ${added.second.partName}")
//                    vpAdapter.addDetails(added.first, added.second)?.let { vpAdapter.notifyItemChanged(it) }
                }
                is RequestResult.SuccessEdit<*> -> {
                    val edited = result.data as Pair<String, SparePart>
                    showToastOnResult("Изменено: ${edited.second.partName}")
                }
                is RequestResult.SuccessDelete<*> -> {
                    val deleted = result.data as Pair<String, SparePart>
                    showSnackBarOnDelete(deleted)
//                    vpAdapter.removeDetails(deleted.first, deleted.second)?.let { vpAdapter.notifyItemChanged(it) }
                }
                is RequestResult.Error -> showError(result.error)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d { "NoteDetailsFragment - onCreateView()" }
        val v = inflater.inflate(R.layout.fragment_note_details_vp, container, false)
        initViews()
        newDetailsDialog = createDialog()
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d { "NoteDetailsFragment - onViewCreated()" }
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        viewModel.loadNotes()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
    }

    override fun onResume() {
        mainActivity.setHomeAction(true) { navController.popBackStack() }
        mainActivity.setFabListener {
            newDetailsDialog.show()
            etNewPartName.showKeyboard(requireContext())
        }
        super.onResume()
    }

    private fun showData(spNote: MutableList<SparePartsNote>) {
//        currentData = spNote
        val sortedList = spNote.sortedWith(notesComparator).toMutableList()
        vpAdapter.dataList = sortedList
        vpAdapter.notifyDataSetChanged()
    }

    private fun showError(error: Throwable?) {
        error?.let { Timber.e(it) { "Ошибка" } }
    }

    private fun initViews() {
        dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_new_details, null)
        etNewPartName = dialogView.findViewById(R.id.et_new_part_name)
        etNewComment = dialogView.findViewById(R.id.et_new_comment)
    }

    private fun initViewPager() {
        vpAdapter = NoteDetailsVPAdapter {editData ->
            when (editData) {
                is FragmentCallback.OnNoteEdited<*> -> viewModel.editNoteDetails(editData.data as Pair<String, SparePart>)
                is FragmentCallback.OnNoteDeleted<*> -> viewModel.deleteDetails(editData.data as Pair<String, SparePart>)
            }
        }

        vp_note_details.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    holderPosition = position
                }
            })
            adapter = vpAdapter
            position?.let { this.currentItem = it } // не работает!!!
        }
    }

    private fun createDialog() = AlertDialog.Builder(requireContext())
        .setTitle(getString(R.string.add_note))
        .setView(dialogView)
        .setPositiveButton(R.string.ok) { dialog, _ ->
            if (etNewPartName.text.isNotEmpty()) {
                viewModel.addNewDetails(vpAdapter.dataList[holderPosition].id to SparePart(
                    id = UUID.randomUUID().toString(),
                    partName = etNewPartName.text.toString(),
                    comment = etNewComment.text.toString()
                ))
                etNewPartName.text.clear()
                etNewComment.text.clear()
                dialog.dismiss()
            }
        }
        .setNegativeButton(getString(R.string.cancel)){ dialog, _ ->
            etNewPartName.text.clear()
            etNewComment.text.clear()
            dialog.dismiss()}
        .create()


    //SnackBar с возможностью восстановления после удаления
    private fun showSnackBarOnDelete(details: Pair<String, SparePart>) {
        Snackbar.make(requireView(), "Удалено", Snackbar.LENGTH_LONG)
            .setAction("Отмена") { v -> viewModel.addNewDetails(details) }.show()
    }

    private fun showToastOnResult(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}
