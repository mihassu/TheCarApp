package ru.mihassu.thecarapp.data.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class SparePart(
    val id: String = UUID.randomUUID().toString(),
    var partName: String = "",
    var comment: String = ""
)  : Parcelable