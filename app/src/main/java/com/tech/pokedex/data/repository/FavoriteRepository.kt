package com.tech.pokedex.data.repository

import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.entity.FavoriteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class FavoriteRepository(private val database: AppDatabase) {
    private val favoriteDao = database.favoriteDao()

    fun getFavorites(userId: String): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites(userId)

    fun isFavorite(id: Int, userId: String): Flow<Boolean> = favoriteDao.isFavorite(id, userId)

    suspend fun toggleFavorite(pokemon: FavoriteEntity, isAlreadyFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            if (isAlreadyFavorite) {
                favoriteDao.removeFavorite(pokemon.id, pokemon.userId)
            } else {
                favoriteDao.addFavorite(pokemon)
            }
        }
    }
}