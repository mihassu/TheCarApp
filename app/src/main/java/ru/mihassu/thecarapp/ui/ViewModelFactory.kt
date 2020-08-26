package ru.mihassu.thecarapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.ui.notedetails.NoteDetailsViewModelVP
import ru.mihassu.thecarapp.ui.notes.NotesViewModel

class ViewModelFactory(val repository : INotesRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>) :T =
        when(modelClass) {
            NotesViewModel::class.java -> NotesViewModel(
                repository
            ) as? T

            NoteDetailsViewModelVP::class.java -> NoteDetailsViewModelVP(
                repository
            ) as? T
            else -> null
        }!!
}