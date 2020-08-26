package ru.mihassu.thecarapp.data.db

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.mihassu.thecarapp.data.IRemoteDataProvider
import ru.mihassu.thecarapp.data.entity.SparePart
import ru.mihassu.thecarapp.data.entity.SparePartsNote
import ru.mihassu.thecarapp.repository.RequestResult

class FireStoreProvider(db: FirebaseFirestore) : IRemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val DETAILS_COLLECTION = "details"
    }

    private val notesRef= db.collection(NOTES_COLLECTION)

    private fun subscribeToNotes() : Observable<MutableList<SparePartsNote>> = Observable.create<MutableList<SparePartsNote>> { emitter ->
        try {
            var notes: List<NoteDb>
            var listNotes: MutableList<SparePartsNote>
            notesRef.addSnapshotListener { snapshotNotes, error ->
                error?.let { emitter.onError(it) } ?: snapshotNotes?.let { querySnapshot ->
                    notes = querySnapshot.documents.map { doc -> doc.toObject(NoteDb::class.java)!! }
                    listNotes = notes.map { n -> SparePartsNote(n.id, n.date, n.mileage) }.toMutableList()
                    emitter.onNext(listNotes)
                }
            }
        } catch (e: Throwable) {
            emitter.onError(e)
        }}.subscribeOn(Schedulers.io())

    fun subscribeToDetails() : Observable<RequestResult> = Observable.create<RequestResult> { emitter ->
        try {
            subscribeToNotes().subscribe { notes ->
                for (i in 0 until notes.size) {
                    notesRef.document(notes[i].id).collection(DETAILS_COLLECTION)
                        .addSnapshotListener { snapshotDetails, error ->
                            error?.let { emitter.onError(it) } ?: snapshotDetails?.
                            let { querySnapshot ->
                                val listDetails = querySnapshot.documents.map { doc -> doc.toObject(SparePart::class.java)!! }.toMutableList()
                                notes[i] = notes[i].copy(detailsList = listDetails)
                            }
                        }
                }
                emitter.onNext(RequestResult.SuccessLoad(notes))
            }
        } catch (e: Throwable) {
            emitter.onError(e)
        }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    override fun subscribeToAll() : Observable<RequestResult> = Observable.create<RequestResult> { emitter ->
        try {
            subscribeToNotes().subscribe { notes ->
                for (i in 0 until notes.size) {
                    notesRef.document(notes[i].id).collection(DETAILS_COLLECTION)
                        .addSnapshotListener { snapshotDetails, error ->
                            error?.let { emitter.onError(it) } ?: snapshotDetails?.let { querySnapshot ->
                                val listDetails: MutableList<SparePart> = querySnapshot.documents.map { doc ->
                                    doc.toObject(SparePart::class.java)!! }.toMutableList()
                                notes[i] = notes[i].copy(detailsList = listDetails)
                                emitter.onNext(RequestResult.SuccessLoad(notes))
                            }
                        }
                }
            }
        } catch (e: Throwable) {
            emitter.onError(e)
        }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun getNotes(): Observable<RequestResult> = Observable.create<RequestResult> { emitter ->
        try {
            var notes: List<NoteDb>
            var listNotes: MutableList<SparePartsNote> = mutableListOf()
            notesRef.addSnapshotListener { snapshotNotes, error ->
                error?.let { emitter.onError(it) } ?: snapshotNotes?.let {querySnapshot ->
                    notes = querySnapshot.documents.map { doc -> doc.toObject(NoteDb::class.java)!! }
                    listNotes = notes.map { n -> SparePartsNote(n.id, n.date, n.mileage) }.toMutableList()
                }
            }

            Thread.sleep(500)

            for (i in 0 until listNotes.size) {
                notesRef.document(listNotes[i].id).collection(DETAILS_COLLECTION).addSnapshotListener { snapshotDetails, error ->
                    error?.let { emitter.onError(it) } ?:
                    snapshotDetails?.let { querySnapshot ->
                        val listDetails = querySnapshot.documents.map { doc -> doc.toObject(SparePart::class.java)!! }.toMutableList()
                        listNotes[i] = listNotes[i].copy(detailsList = listDetails)
                    }
                }
            }
            Thread.sleep(500)
            emitter.onNext(RequestResult.SuccessLoad(listNotes))

        } catch (e: Throwable) {
            emitter.onError(e)
        }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    fun getNotes1(): Single<RequestResult> = Single.create<RequestResult>{emitter ->
        notesRef.get()
            .addOnSuccessListener { querySnapshot -> querySnapshot?.let {
                val notes = mutableListOf<SparePartsNote>()
                for (n in it) {
                    notes.add(n.toObject(SparePartsNote::class.java))
                }
                emitter.onSuccess(RequestResult.SuccessLoad(notes)) }
            }.addOnFailureListener { exception -> emitter.onError(exception) }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun deleteNote(note: SparePartsNote): Single<RequestResult> = Single.create<RequestResult> { emitter ->
        notesRef.document(note.id).delete()
            .addOnSuccessListener {
//                try {
//                    val future = notesRef.limit(20).get()
//                    val docs = future.result!!.documents
//                    for (d in docs) {
//                        d.reference.delete()
//                    }
//
//                }
                emitter.onSuccess(RequestResult.SuccessDelete(note))
            }
            .addOnFailureListener { exception -> emitter.onError(exception) }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun addNote(note: SparePartsNote): Single<RequestResult> = Single.create<RequestResult> { emitter ->
        notesRef.document(note.id).set(note)
            .addOnSuccessListener { emitter.onSuccess(RequestResult.SuccessAdd(note)) }
            .addOnFailureListener { exception -> emitter.onError(exception) }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun editNote(note: SparePartsNote): Single<RequestResult> = Single.create<RequestResult> { emitter ->
        notesRef.document(note.id).set(note)
            .addOnSuccessListener { emitter.onSuccess(RequestResult.SuccessEdit(note)) }
            .addOnFailureListener { exception -> emitter.onError(exception) }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun addNewDetails(details: Pair<String, SparePart>): Single<RequestResult> = Single.create<RequestResult> { emitter ->
//        notesRef.document(details.first).update("detailsList", details.second)
        notesRef.document(details.first).collection(DETAILS_COLLECTION).document(details.second.id).set(details.second)
            .addOnSuccessListener { emitter.onSuccess(RequestResult.SuccessAdd(details)) }
            .addOnFailureListener { exception -> emitter.onError(exception) }

    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun editDetails(details: Pair<String, SparePart>): Single<RequestResult> = Single.create<RequestResult> { emitter ->
        notesRef.document(details.first).collection(DETAILS_COLLECTION).document(details.second.id).set(details.second)
            .addOnSuccessListener { emitter.onSuccess(RequestResult.SuccessEdit(details)) }
            .addOnFailureListener { exception -> emitter.onError(exception) }

    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())


    override fun deleteDetails(details: Pair<String, SparePart>): Single<RequestResult> = Single.create<RequestResult> { emitter ->
        notesRef.document(details.first).collection(DETAILS_COLLECTION).document(details.second.id).delete()
            .addOnSuccessListener { emitter.onSuccess(RequestResult.SuccessDelete(details)) }
            .addOnFailureListener { exception -> emitter.onError(exception) }

    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}