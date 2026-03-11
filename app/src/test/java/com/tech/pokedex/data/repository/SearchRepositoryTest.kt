package com.tech.pokedex.data.repository

import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.dao.PokemonDao
import com.tech.pokedex.data.local.dao.SearchHistoryDao
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.local.entity.SearchHistoryEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchRepositoryTest {

    private lateinit var repository: SearchRepository
    private lateinit var database: AppDatabase
    private lateinit var pokemonDao: PokemonDao
    private lateinit var searchHistoryDao: SearchHistoryDao

    @Before
    fun setUp() {
        database = mockk()
        pokemonDao = mockk(relaxed = true)
        searchHistoryDao = mockk(relaxed = true)
        
        every { database.pokemonDao() } returns pokemonDao
        every { database.searchHistoryDao() } returns searchHistoryDao
        
        repository = SearchRepository(database)
    }

    @Test
    fun `searchPokemonLocal calls pokemonDao searchPokemonLocal`() = runTest {
        val query = "Pika"
        val dummyResults = listOf(mockk<PokemonEntity>())
        coEvery { pokemonDao.searchPokemonLocal(query) } returns dummyResults
        
        val result = repository.searchPokemonLocal(query)
        
        assertEquals(dummyResults, result)
        coVerify { pokemonDao.searchPokemonLocal(query) }
    }

    @Test
    fun `getRecentSearches returns flow from searchHistoryDao`() = runTest {
        val dummyHistory = listOf(SearchHistoryEntity("query1"))
        every { searchHistoryDao.getRecentSearches() } returns flowOf(dummyHistory)
        
        repository.getRecentSearches().collect { result ->
            assertEquals(dummyHistory, result)
        }
    }

    @Test
    fun `insertSearchQuery calls searchHistoryDao insertSearchQuery`() = runTest {
        val query = " Pikachu "
        repository.insertSearchQuery(query)
        coVerify { searchHistoryDao.insertSearchQuery(match { it.query == "Pikachu" }) }
    }

    @Test
    fun `deleteSearchQuery calls searchHistoryDao deleteSearchQuery`() = runTest {
        val query = "Charmander"
        repository.deleteSearchQuery(query)
        coVerify { searchHistoryDao.deleteSearchQuery(query) }
    }

    @Test
    fun `clearAllHistory calls searchHistoryDao clearAllHistory`() = runTest {
        repository.clearAllHistory()
        coVerify { searchHistoryDao.clearAllHistory() }
    }
}
