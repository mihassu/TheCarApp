package ru.mihassu.thecarapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import ru.mihassu.thecarapp.repository.RequestResult

object NotesViewState {

    private var notesLiveData = MutableLiveData<RequestResult>()

    fun setNotesLiveData(data: RequestResult) {
        notesLiveData.value = data
    }

    fun getNotesLiveData() : LiveData<RequestResult> = notesLiveData

}