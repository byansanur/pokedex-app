package com.tech.pokedex.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tech.pokedex.data.local.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(pokemon: FavoriteEntity)

    @Query("DELETE FROM favorite_pokemon WHERE id = :pokemonId AND userId = :userId")
    suspend fun removeFavorite(pokemonId: Int, userId: String)

    @Query("SELECT * FROM favorite_pokemon WHERE userId = :userId ORDER BY timestamp DESC")
    fun getAllFavorites(userId: String): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pokemon WHERE id = :pokemonId AND userId = :userId)")
    fun isFavorite(pokemonId: Int, userId: String): Flow<Boolean>
}