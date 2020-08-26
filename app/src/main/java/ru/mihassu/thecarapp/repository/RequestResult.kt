package ru.mihassu.thecarapp.repository

sealed class RequestResult {
    data class SuccessLoad<out T>(val data: T) : RequestResult()
    data class SuccessAdd<out T>(val data: T) : RequestResult()
    data class SuccessDelete<out T>(val data: T) : RequestResult()
    data class SuccessEdit<out T>(val data: T) : RequestResult()
    data class Error(val error: Throwable?) : RequestResult()
}