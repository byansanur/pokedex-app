package com.tech.pokedex.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tech.pokedex.data.local.dao.FavoriteDao
import com.tech.pokedex.data.local.dao.PokemonDao
import com.tech.pokedex.data.local.dao.SearchHistoryDao
import com.tech.pokedex.data.local.dao.UserDao
import com.tech.pokedex.data.local.entity.FavoriteEntity
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.local.entity.SearchHistoryEntity
import com.tech.pokedex.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, PokemonEntity::class, SearchHistoryEntity::class, FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao() : UserDao
    abstract fun pokemonDao() : PokemonDao
    abstract fun searchHistoryDao() : SearchHistoryDao
    abstract fun favoriteDao() : FavoriteDao

}