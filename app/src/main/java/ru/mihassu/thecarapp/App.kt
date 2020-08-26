package ru.mihassu.thecarapp

import android.app.Application
import com.github.ajalt.timberkt.Timber
import ru.mihassu.thecarapp.di.AppComponent
import ru.mihassu.thecarapp.di.DaggerAppComponent
import ru.mihassu.thecarapp.di.FireStoreModule
import ru.mihassu.thecarapp.di.RepositoryModule

class App : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        Timber.plant(timber.log.Timber.DebugTree())
        Timber.tag("CarApp")

        appComponent = DaggerAppComponent.builder()
            .fireStoreModule(FireStoreModule())
            .repositoryModule(RepositoryModule())
            .build()
    }

    fun getAppComponent() = appComponent
}