package com.tech.pokedex.ui.viewmodel

import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.repository.SearchRepository
import com.tech.pokedex.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: SearchRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        every { repository.getRecentSearches() } returns flowOf(emptyList())
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `onSearchQueryChange updates searchQuery state`() {
        val query = "Pikachu"
        viewModel.onSearchQueryChange(query)
        assertEquals(query, viewModel.searchQuery.value)
    }

    @Test
    fun `searching with less than 3 characters returns empty list`() = runTest {
        viewModel.onSearchQueryChange("Pi")
        advanceTimeBy(600) // Pass the 500ms debounce
        
        assertTrue(viewModel.searchResults.value.isEmpty())
        coVerify(exactly = 0) { repository.searchPokemonLocal(any()) }
    }

    @Test
    fun `searching with valid query returns results from repository`() = runTest {
        val query = "Pikachu"
        val dummyResults = listOf(mockk<PokemonEntity>(relaxed = true))
        
        coEvery { repository.searchPokemonLocal(query) } returns dummyResults
        
        viewModel.onSearchQueryChange(query)
        advanceTimeBy(600) // Wait for debounce
        
        assertEquals(dummyResults, viewModel.searchResults.value)
    }

    @Test
    fun `saveSearchQuery calls repository insertSearchQuery`() = runTest {
        val query = "Charmander"
        viewModel.saveSearchQuery(query)
        coVerify { repository.insertSearchQuery(query) }
    }

    @Test
    fun `deleteRecentSearch calls repository deleteSearchQuery`() = runTest {
        val query = "Bulbasaur"
        viewModel.deleteRecentSearch(query)
        coVerify { repository.deleteSearchQuery(query) }
    }

    @Test
    fun `clearAllRecentSearches calls repository clearAllHistory`() = runTest {
        viewModel.clearAllRecentSearches()
        coVerify { repository.clearAllHistory() }
    }
}
