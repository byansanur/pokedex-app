package com.tech.pokedex.data.repository

import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.entity.FavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FavoriteRepository(private val database: AppDatabase) {
    private val favoriteDao = database.favoriteDao()

    fun getFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()

    fun isFavorite(id: Int): Flow<Boolean> = favoriteDao.isFavorite(id)

    suspend fun toggleFavorite(pokemon: FavoriteEntity, isAlreadyFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            if (isAlreadyFavorite) {
                favoriteDao.removeFavorite(pokemon.id)
            } else {
                favoriteDao.addFavorite(pokemon)
            }
        }
    }
}