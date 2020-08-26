package ru.mihassu.thecarapp.ui.notes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_notes.*
import ru.mihassu.thecarapp.App
import ru.mihassu.thecarapp.R
import ru.mihassu.thecarapp.data.db.FireStoreProvider
import ru.mihassu.thecarapp.data.entity.SparePartsNote
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.repository.NotesRepositoryImpl
import ru.mihassu.thecarapp.repository.RequestResult
import ru.mihassu.thecarapp.repository.SparePartsRepositoryImpl
import ru.mihassu.thecarapp.ui.*
import ru.mihassu.thecarapp.util.notesComparator
import ru.mihassu.thecarapp.util.setInputValidation
import ru.mihassu.thecarapp.util.showKeyboard
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class NotesFragment : Fragment() {

    companion object {
        val NOTE_ID_EXTRA = "noteId"
        val POSITION_EXTRA = "position"
        val ITEM_REMOVE = 0
        val ITEM_INSERT = 1
        val ITEM_CHANGE = 2
    }

    @Inject
    lateinit var repository : INotesRepository

    private lateinit var mainActivity: FragmentActionListener
    private lateinit var navController: NavController
    private lateinit var notesRVAdapter: NotesRVAdapter
    private lateinit var dialogView: View
    private lateinit var etNewNoteDate: EditText
    private lateinit var etNewNoteMileage: EditText
    private lateinit var newNoteDialog: AlertDialog
    private val patternDate = Pattern.compile("\\d{2}[.]\\d{2}[.]\\d{4}")
    private val patternMileage = Pattern.compile("\\d{6}")
    private lateinit var tempNote: SparePartsNote


//    private lateinit var showAnim: Animator
//    private lateinit var hideAnim: Animator


    private val viewModel: NotesViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(repository)).get(NotesViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as FragmentActionListener
        Timber.d { "NotesFragment - onAttach()" }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as App).getAppComponent().inject(this)

//        NotesViewState.getNotesLiveData().observe(this, Observer { result ->
//            when(result) {
//
//            }
//        })

        viewModel.getSpNotesLiveData().observe(this, Observer { result ->
            when (result) {
                is RequestResult.SuccessLoad<*> -> (result.data as MutableList<SparePartsNote>)?.let { showData(it) }
                is RequestResult.SuccessAdd<*> -> {
                    val added = result.data as SparePartsNote
                    showToastOnResult("Добавлено: ${added.date}")
//                    notesRVAdapter.addNewItem(result.data as SparePartsNote).let { notesRVAdapter.notifyItemInserted(it) }
                }
                is RequestResult.SuccessDelete<*> -> {
                    val deletedNote = result.data as SparePartsNote
//                    notesRVAdapter.deleteItem(deletedNote)?.let { notesRVAdapter.notifyItemRemoved(it) }
                    showSnackBarOnDelete(deletedNote)
                }
                is RequestResult.Error -> showError(result.error)
            }
        })
        lifecycle.addObserver(viewModel)
        Timber.d { "NotesFragment - onCreate()" }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_notes, container, false)
        initViews()
//        showAnim = AnimatorInflater.loadAnimator(activity, R.anim.slide_to_right_show).apply { setTarget(v) }
//        hideAnim = AnimatorInflater.loadAnimator(activity, R.anim.slide_to_left_hide).apply { setTarget(v) }
        Timber.d { "NotesFragment - onCreateView()" }
        return v
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        Timber.d { "NotesFragment - onViewCreated()" }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
        Timber.d { "NotesFragment - onActivityCreated()" }
    }

    override fun onResume() {
        mainActivity.setHomeAction(false) { true }
        mainActivity.setFabListener {
            newNoteDialog.show()
            etNewNoteDate.showKeyboard(requireContext())
        }
        super.onResume()
    }

    private fun initViews() {
        dialogView = requireActivity().layoutInflater.inflate(R.layout.dialog_new_note, null)
        etNewNoteDate = dialogView.findViewById(R.id.et_new_date)
        etNewNoteMileage = dialogView.findViewById(R.id.et_new_mileage)
        newNoteDialog = createDialog()
        etNewNoteDate.setInputValidation(patternDate, "Неверный формат")
        etNewNoteMileage.setInputValidation(patternMileage, "Неверный формат")
    }

    private fun initRecyclerView() {
        notesRVAdapter = NotesRVAdapter { callback ->
            when (callback) {
                is FragmentCallback.OnNoteEdited<*> -> viewModel.editNote(callback.data as SparePartsNote)
                is FragmentCallback.OnNoteDeleted<*> -> viewModel.deleteNote(callback.data as SparePartsNote)
            }
        }
        //слушатель на слик по элементу - прейти на фрагмент с подробностями
        notesRVAdapter.onNoteClickListener = object : NotesRVAdapter.OnNoteClickListener {
            override fun onNoteClick(position: Int) {
                val bundle = Bundle().apply { putInt(POSITION_EXTRA, position) }
                navController.navigate(R.id.action_SparePartsFragment_to_noteDetailsFragmentVP, bundle)
            }
        }

        //RecyclerView с коллбэком на свайп
        rv_notes.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = notesRVAdapter
            val itemTouchCallback = ItemTouchCallback(notesRVAdapter)
            val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    private fun showData(spNotes: MutableList<SparePartsNote>) {
        val sortList = spNotes.sortedWith(notesComparator)
        notesRVAdapter.dataList = sortList.toMutableList()
        notesRVAdapter.notifyDataSetChanged()
    }

    private fun showError(error: Throwable?) {
        error?.let { Timber.e(it) { "Ошибка" } }
    }

    //SnackBar с возможностью восстановления после удаления
    private fun showSnackBarOnDelete(note: SparePartsNote) {
        Snackbar.make(requireView(), "Удалено", Snackbar.LENGTH_LONG)
            .setAction("Отмена") { v -> viewModel.addNewNote(note) }.show()
    }


    private fun createDialog() = AlertDialog.Builder(requireContext())
        .setTitle(getString(R.string.add_note))
        .setView(dialogView)
        .setPositiveButton(getString(R.string.ok)) { dialog, _ ->

            if (etNewNoteDate.text.isNotEmpty() && etNewNoteMileage.text.isNotEmpty()) {
                viewModel.addNewNote(
                    SparePartsNote(
                        UUID.randomUUID().toString(),
                        etNewNoteDate.text.toString(),
                        (Integer.parseInt(etNewNoteMileage.text.toString())).toLong(),
                        mutableListOf()
                    )
                )
                etNewNoteDate.text.clear()
                etNewNoteMileage.text.clear()
                dialog.dismiss()
            }
        }
        .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            etNewNoteDate.text.clear()
            etNewNoteMileage.text.clear()
            dialog.dismiss()
        }
        .create()

    private fun showToastOnResult(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}
