package ru.mihassu.thecarapp.data.entity

import java.util.*

data class Car(val id: String = UUID.randomUUID().toString(),
               val name: String,
               val color: Int = 0xffffff) {
}