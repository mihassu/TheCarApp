package ru.mihassu.thecarapp.di

import dagger.Module
import dagger.Provides
import ru.mihassu.thecarapp.data.IRemoteDataProvider
import ru.mihassu.thecarapp.repository.INotesRepository
import ru.mihassu.thecarapp.repository.NotesRepositoryImpl
import javax.inject.Singleton


@Module (includes = [FireStoreModule::class])
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(provider: IRemoteDataProvider) : INotesRepository = NotesRepositoryImpl(provider)
}