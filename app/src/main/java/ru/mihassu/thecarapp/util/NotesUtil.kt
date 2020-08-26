package ru.mihassu.thecarapp.util

import ru.mihassu.thecarapp.data.entity.SparePartsNote
import java.util.regex.Pattern

val notesComparator = Comparator<SparePartsNote> { o1, o2 ->
    val splitDate1 = o1.date.split(Pattern.compile("[.]"), 3).apply { map { Integer.parseInt(it) } }
    val splitDate2 = o2.date.split(Pattern.compile("[.]"), 3).apply { map { Integer.parseInt(it) } }
    for (i in 2 downTo  0) {
        if (splitDate1[i] > splitDate2[i]) return@Comparator 1
        else if (splitDate1[i] < splitDate2[i]) return@Comparator -1
    }
    return@Comparator 0
}