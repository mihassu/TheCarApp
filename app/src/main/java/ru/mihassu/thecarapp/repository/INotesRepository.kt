package ru.mihassu.thecarapp.repository

import io.reactivex.Observable
import io.reactivex.Single
import ru.mihassu.thecarapp.data.entity.SparePart
import ru.mihassu.thecarapp.data.entity.SparePartsNote

interface INotesRepository {

    fun subscribeToAll() : Observable<RequestResult>
    fun getNotes() : Observable<RequestResult>

    fun addNote(note: SparePartsNote): Single<RequestResult>
    fun editNote(note: SparePartsNote) : Single<RequestResult>
    fun deleteNote(note: SparePartsNote): Single<RequestResult>

    fun addNewDetails(details: Pair<String, SparePart>) : Single<RequestResult>
    fun editDetails(details: Pair<String, SparePart>) : Single<RequestResult>
    fun deleteDetails(details: Pair<String, SparePart>) : Single<RequestResult>
}