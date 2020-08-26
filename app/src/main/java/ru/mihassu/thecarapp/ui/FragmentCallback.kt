package ru.mihassu.thecarapp.ui

sealed class FragmentCallback {
    class OnNoteEdited<T>(val data: T) : FragmentCallback()
    class OnNoteDeleted<T>(val data: T): FragmentCallback()
}