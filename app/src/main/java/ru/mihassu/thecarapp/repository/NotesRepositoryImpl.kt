package ru.mihassu.thecarapp.repository

import io.reactivex.Observable
import io.reactivex.Single
import ru.mihassu.thecarapp.data.IRemoteDataProvider
import ru.mihassu.thecarapp.data.entity.SparePart
import ru.mihassu.thecarapp.data.entity.SparePartsNote

class NotesRepositoryImpl(private val provider: IRemoteDataProvider) : INotesRepository {

    override fun subscribeToAll() : Observable<RequestResult> = provider.subscribeToAll()
    override fun getNotes(): Observable<RequestResult> = provider.getNotes()
    override fun addNote(note: SparePartsNote): Single<RequestResult> = provider.addNote(note)
    override fun editNote(note: SparePartsNote): Single<RequestResult> = provider.editNote(note)
    override fun deleteNote(note: SparePartsNote): Single<RequestResult> = provider.deleteNote(note)
    override fun addNewDetails(details: Pair<String, SparePart>): Single<RequestResult> = provider.addNewDetails(details)
    override fun editDetails(details: Pair<String, SparePart>): Single<RequestResult> = provider.editDetails(details)
    override fun deleteDetails(details: Pair<String, SparePart>): Single<RequestResult> = provider.deleteDetails(details)
}