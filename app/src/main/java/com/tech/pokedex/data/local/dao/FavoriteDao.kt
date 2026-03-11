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

    @Query("DELETE FROM favorite_pokemon WHERE id = :pokemonId")
    suspend fun removeFavorite(pokemonId: Int)

    @Query("SELECT * FROM favorite_pokemon ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_pokemon WHERE id = :pokemonId)")
    fun isFavorite(pokemonId: Int): Flow<Boolean>
}