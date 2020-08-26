package ru.mihassu.thecarapp.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*
import java.util.regex.Pattern

@Parcelize
data class SparePartsNote(val id: String = UUID.randomUUID().toString(),
                          val date: String = "",
                          val mileage: Long = 0,
                          val detailsList: MutableList<SparePart> = mutableListOf()) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (this.javaClass != other?.javaClass) return false

        other as SparePartsNote
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "SparePartNote {\nid: $id, \ndate: $date, \nmileage: $mileage, \npartList: ${detailsList.toString()}"
    }


}

fun SparePartsNote.compare(note: SparePartsNote) : Int {
    val splitDate1 = date.split(Pattern.compile("[.]"), 3).apply { map { Integer.parseInt(it) } }
    val splitDate2 = note.date.split(Pattern.compile("[.]"), 3).apply { map { Integer.parseInt(it) } }
    for (i in 2 downTo  0) {
        if (splitDate1[i] > splitDate2[i]) return 1
        else if (splitDate1[i] < splitDate2[i]) return -1
    }
    return 0
}