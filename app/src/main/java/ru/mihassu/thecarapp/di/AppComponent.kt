package ru.mihassu.thecarapp.di

import dagger.Component
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.ui.notedetails.NoteDetailsFragmentVP
import ru.mihassu.thecarapp.ui.notes.NotesFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [FireStoreModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(fragment: NotesFragment)
    fun inject(fragment: NoteDetailsFragmentVP)

    fun getRepository() : INotesRepository
}