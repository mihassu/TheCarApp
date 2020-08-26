package ru.mihassu.thecarapp.ui.notedetails

import androidx.lifecycle.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import ru.mihassu.thecarapp.data.entity.SparePart
import ru.mihassu.thecarapp.data.entity.SparePartsNote
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.repository.RequestResult

class NoteDetailsViewModelVP(private val repository: INotesRepository) : ViewModel(), LifecycleObserver {

    private val detailsLiveData = MutableLiveData<RequestResult>()
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun getNotesLiveData(): LiveData<RequestResult> = detailsLiveData

    fun loadNotes() {

        repository.subscribeToAll().subscribeWith(object : DisposableObserver<RequestResult>() {
            override fun onComplete() {

            }

            override fun onNext(t: RequestResult) {
                detailsLiveData.value = t
            }

            override fun onError(e: Throwable) {
                detailsLiveData.value = RequestResult.Error(e)
            }
        }).apply { disposables.add(this) }

    }

    fun editNoteDetails(editList: Pair<String, SparePart>) {
        disposables.add(repository.editDetails(editList).subscribe { edited, err ->
            err?.let { detailsLiveData.value = RequestResult.Error(it) } ?:
                    edited?.let { detailsLiveData.value = it }
        })
    }

    fun addNewDetails(details: Pair<String, SparePart>) {
        disposables.add(repository.addNewDetails(details).subscribe { added, err ->
            err?.let { detailsLiveData.value = RequestResult.Error(it) } ?:
                    added?.let { detailsLiveData.value = it }
        })
    }

    fun deleteDetails(details: Pair<String, SparePart>) {
        disposables.add(repository.deleteDetails(details).subscribe { added, err ->
            err?.let { detailsLiveData.value = RequestResult.Error(it) } ?:
            added?.let { detailsLiveData.value = it }
        })
    }

    fun editNote(note: SparePartsNote) {
        disposables.add(repository.editNote(note).subscribe { edited, err ->
            err?.let { detailsLiveData.value = RequestResult.Error(it) } ?:
            edited?.let { detailsLiveData.value = it }
        })
    }

    override fun onCleared() {
        disposables.dispose()
    }
}
