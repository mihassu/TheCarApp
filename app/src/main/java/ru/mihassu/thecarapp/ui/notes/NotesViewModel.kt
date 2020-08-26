package ru.mihassu.thecarapp.ui.notes

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import ru.mihassu.thecarapp.data.entity.SparePartsNote
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.repository.RequestResult

class NotesViewModel(private val repository: INotesRepository) : ViewModel(), LifecycleObserver {

    private val spNotesLiveData = MutableLiveData<RequestResult>()
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun getSpNotesLiveData() : LiveData<RequestResult> = spNotesLiveData

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun loadSpNotes() {
        disposables.add(
            repository.subscribeToAll().subscribeWith(object : DisposableObserver<RequestResult>() {
                override fun onComplete() {

                }

                override fun onNext(t: RequestResult) {
                    spNotesLiveData.value = t
                }

                override fun onError(e: Throwable) {
                    spNotesLiveData.value = RequestResult.Error(e)
                }
            })
        )
    }

    fun editNote(note: SparePartsNote) {

    }

    fun deleteNote(note: SparePartsNote) {
        disposables.add(repository.deleteNote(note).subscribe {removed, err ->
            err?.let { RequestResult.Error(it) } ?:
            removed?.let { spNotesLiveData.value = it }
        })
    }

    fun addNewNote(note: SparePartsNote) {
        disposables.add(repository.addNote(note).subscribe { added, err ->
            err?.let { RequestResult.Error(it)  } ?:
            added?.let { spNotesLiveData.value = it}
        })
    }


    override fun onCleared() {
        disposables.dispose()
    }
}
