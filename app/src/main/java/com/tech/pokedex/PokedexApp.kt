package com.tech.pokedex

import android.app.Application
import com.tech.pokedex.di.databaseModule
import com.tech.pokedex.di.networkModule
import com.tech.pokedex.di.repositoryModule
import com.tech.pokedex.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PokedexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PokedexApp)
            modules(
                databaseModule,
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }
}