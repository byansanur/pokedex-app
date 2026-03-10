package com.tech.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tech.pokedex.data.local.dao.PokemonDao
import com.tech.pokedex.data.local.dao.UserDao
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun pokemonDao() : PokemonDao

}