package com.tech.pokedex.di

import android.content.Context
import androidx.room.Room
import com.tech.pokedex.BuildConfig
import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.SessionManager
import net.zetetic.database.sqlcipher.SupportOpenHelperFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun provideDatabase(context: Context): AppDatabase {
    System.loadLibrary("sqlcipher")
    val secretKey = BuildConfig.DB_SECRET
    val passphrase = secretKey.toByteArray(Charsets.UTF_8)
    val factory = SupportOpenHelperFactory(passphrase)

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "pokedexapp"
    )
        .openHelperFactory(factory)
        .fallbackToDestructiveMigration()
        .build()
}

val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().pokemonDao() }
    single { get<AppDatabase>().searchHistoryDao() }
    single { get<AppDatabase>().favoriteDao() }

    single { SessionManager(context = androidContext()) }

}