package ru.mihassu.thecarapp.usecase

import ru.mihassu.thecarapp.data.entity.SparePartsNote
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.repository.SparePartsRepositoryImpl

class NotesInteractor(val repository: INotesRepository) {

    var tempNote: SparePartsNote? = null

    fun deleteNote(note: SparePartsNote) {
        tempNote = note
    }

    fun restoreNote() {

    }

    fun loadNotes() {

    }

    fun addNote(note: SparePartsNote) {

    }


}