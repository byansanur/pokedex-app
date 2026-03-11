package com.tech.pokedex.data.repository

import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SearchRepository(
    private val database: AppDatabase
) {
    private val pokemonDao = database.pokemonDao()
    private val searchHistoryDao = database.searchHistoryDao()

    suspend fun searchPokemonLocal(query: String): List<PokemonEntity> = withContext(Dispatchers.IO) {
        pokemonDao.searchPokemonLocal(query)
    }

    fun getRecentSearches(): Flow<List<SearchHistoryEntity>> {
        return searchHistoryDao.getRecentSearches()
    }

    suspend fun insertSearchQuery(query: String) = withContext(Dispatchers.IO) {
        searchHistoryDao.insertSearchQuery(SearchHistoryEntity(query = query.trim()))
    }

    suspend fun deleteSearchQuery(query: String) = withContext(Dispatchers.IO) {
        searchHistoryDao.deleteSearchQuery(query)
    }

    suspend fun clearAllHistory() = withContext(Dispatchers.IO) {
        searchHistoryDao.clearAllHistory()
    }
}