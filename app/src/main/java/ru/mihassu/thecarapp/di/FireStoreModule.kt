package ru.mihassu.thecarapp.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import ru.mihassu.thecarapp.data.IRemoteDataProvider
import ru.mihassu.thecarapp.data.db.FireStoreProvider
import javax.inject.Singleton


@Module
class FireStoreModule {

    @Provides
    @Singleton
    fun provideDataBase() : FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFireStoreProvider(db: FirebaseFirestore) : IRemoteDataProvider = FireStoreProvider(db)
}