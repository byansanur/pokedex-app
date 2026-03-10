package com.tech.pokedex.di

import android.content.Context
import androidx.room.Room
import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.SessionManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideDatabase(context: Context): AppDatabase {
//    System.loadLibrary("sqlcipher")
//    val secretKey =
    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "pokedexapp"
    )
        .fallbackToDestructiveMigration()
        .build()
}

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().pokemonDao() }

    single { SessionManager(context = androidContext()) }

}