package ru.mihassu.thecarapp.repository

import io.reactivex.Single
import ru.mihassu.thecarapp.data.entity.SparePart
import ru.mihassu.thecarapp.data.entity.SparePartsNote
import java.util.*

object SparePartsRepositoryImpl {

    var notesList: MutableList<SparePartsNote> = mutableListOf(
        SparePartsNote(
            id = "01",
            date = "20.06.2020",
            mileage = 37400,
            detailsList = mutableListOf(
                SparePart(
                    id = "11",
                    partName = "Масло",
                    comment = "Заменено масло на сервисе"
                ),
                SparePart(
                    id = "22",
                    partName = "Передние колодки",
                    comment = "Зааменены колодки, цена 2600 руб."
                )
            )
        ),
        SparePartsNote(
            id = UUID.randomUUID().toString(),
            date = "20.07.2020",
            mileage = 47500,
            detailsList = mutableListOf(
                SparePart(
                    id = UUID.randomUUID().toString(),
                    partName = "Задние колодки",
                    comment = "цена 3000 руб."
                )
            )
        ),
        SparePartsNote(
            id = UUID.randomUUID().toString(),
            date = "20.01.2020",
            mileage = 17500,
            detailsList = mutableListOf(
                SparePart(
                    id = UUID.randomUUID().toString(), partName = "rtewtgeи", comment = "t3ty4yh3h"
                )
            )
        ),
        SparePartsNote(
            id = UUID.randomUUID().toString(),
            date = "02.02.2020",
            mileage = 1500,
            detailsList = mutableListOf(
                SparePart(
                    id = UUID.randomUUID().toString(), partName = "ewqqe", comment = "123"
                )
            )
        ),
        SparePartsNote(
            id = UUID.randomUUID().toString(),
            date = "01.11.2010",
            mileage = 13500,
            detailsList = mutableListOf(
                SparePart(
                    id = UUID.randomUUID().toString(), partName = "pio", comment = "iuoh"
                )
            )
        )
    )

     fun getNotes(): Single<List<SparePartsNote>> {
        val copyNotes = mutableListOf<SparePartsNote>()
        notesList.forEach {note ->
            val copyDetails = mutableListOf<SparePart>()
            for (details in note.detailsList) {
                copyDetails.add(details.copy())
            }
            copyNotes.add(note.copy(detailsList = copyDetails))
        }
        return Single.just(copyNotes)
    }

     fun deleteNote(note: SparePartsNote) = Single.create<SparePartsNote> { emitter ->
        if (notesList.removeIf { it.id == note.id }) {
            emitter.onSuccess(note)
        } else emitter.onError(Throwable("Ошибка"))
    }


     fun addNote(note: SparePartsNote) = Single.create<SparePartsNote?> { emitter ->
        notesList.add(note)
        emitter.onSuccess(note)
    }

     fun addNewDetails(details: Pair<String, SparePart>) = Single.create<Pair<String, SparePart>> { emitter ->
        notesList.forEach{
            if (it.id == details.first) {
                it.detailsList.add(details.second)
                emitter.onSuccess(details)
                return@create
            }
        }
        emitter.onError(Throwable("Ошибка"))
    }

     fun editDetails(details: Pair<String, SparePart>) = Single.create<Pair<String, SparePart>> { emitter ->
        //Найти нужную запись
        for (i in 0 until notesList.size) {
            if (notesList[i].id == details.first) {
                //Найти список подробностей
                for (j in 0 until notesList[i].detailsList.size) {
                    if (notesList[i].detailsList[j].id == details.second.id) {
                        //заменить список подробностей
                        notesList[i].detailsList[j] = details.second
                        emitter.onSuccess(details)
                        return@create
                    }
                }
            }
        }
        emitter.onError(Throwable("Ошибка"))
    }

     fun deleteDetails(details: Pair<String, SparePart>) = Single.create<Pair<String, SparePart>> { emitter ->
        //Найти нужную запись
        for (i in 0 until notesList.size) {
            if (notesList[i].id == details.first) {
                //Найти список подробностей
                for (j in 0 until notesList[i].detailsList.size) {
                    if (notesList[i].detailsList[j].id == details.second.id) {
                        //удалить список подробностей
                        notesList[i].detailsList.removeAt(j)
                        emitter.onSuccess(details)
                        return@create
                    }
                }
            }
        }
        emitter.onError(Throwable("Ошибка"))
    }

     fun editNote(note: SparePartsNote) = Single.create<SparePartsNote> { emitter ->
        for (i in 0 until notesList.size) {
            if (notesList[i].id == note.id) {
                notesList[i] = notesList[i].copy()
                emitter.onSuccess(note)
                return@create
            }
        }
        emitter.onError(Throwable("Ошибка"))
    }




//    override fun getSparePartById(id: String): Single<SparePartsNote>? =
//
//        notesList
//            .find { it.id == id }
//            .apply { Timber.d { "Запрос id: $id" } }
//            ?.let {
//                Timber.d { "Надена запись id: ${it.id}" }
//                Single.just(it)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//            }
//
//
//    override fun addOrEditSparePart(note: SparePartsNote) {
//
//        for (i in 0 until notesList.size) {
//            if (notesList[i] == note) {
//                notesList[i] = note
//                return
//            }
//        }
//        notesList.add(note)
//    }
}
