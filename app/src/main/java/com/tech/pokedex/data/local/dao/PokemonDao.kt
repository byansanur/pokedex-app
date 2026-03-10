package com.tech.pokedex.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tech.pokedex.data.local.entity.PokemonEntity

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Query("SELECT * FROM pokemon_list ORDER BY id ASC LIMIT :limit OFFSET :offset")
    suspend fun getPokemonList(limit: Int, offset: Int): List<PokemonEntity>

    @Query("SELECT * FROM pokemon_list ORDER BY id ASC")
    fun getPokemonsPagingSource(): PagingSource<Int, PokemonEntity>

    @Query("SELECT COUNT(id) FROM pokemon_list")
    suspend fun getPokemonCount(): Int

    @Query("DELETE FROM pokemon_list")
    suspend fun clearAll()
}