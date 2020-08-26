package ru.mihassu.thecarapp.ui

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.repository.RequestResult

class MainActivityViewModel(private val repository: INotesRepository) : ViewModel(){

    private val disposables: CompositeDisposable = CompositeDisposable()


    fun subscribeToAllNotes() {
        disposables.add(
            repository.subscribeToAll().subscribeWith(object : DisposableObserver<RequestResult>() {
                override fun onComplete() {

                }

                override fun onNext(t: RequestResult) {
                    NotesViewState.setNotesLiveData(t)
                }

                override fun onError(e: Throwable) {
                    NotesViewState.setNotesLiveData(RequestResult.Error(e))
                }
            })
        )
    }


    override fun onCleared() {
        disposables.dispose()
    }
}