package com.tech.pokedex.ui.viewmodel

import com.tech.pokedex.data.local.entity.FavoriteEntity
import com.tech.pokedex.data.repository.FavoriteRepository
import com.tech.pokedex.util.MainDispatcherRule
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var repository: FavoriteRepository

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        every { repository.getFavorites() } returns flowOf(emptyList())
        viewModel = FavoriteViewModel(repository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `favorites state initialized from repository`() = runTest {
        val dummyFavorites = listOf(mockk<FavoriteEntity>(relaxed = true))
        every { repository.getFavorites() } returns flowOf(dummyFavorites)
        
        // Need to recreate ViewModel because favorites is a stateIn initialized at init
        val newViewModel = FavoriteViewModel(repository)
        
        // Start collecting to trigger WhileSubscribed
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            newViewModel.favorites.collect()
        }
        
        assertEquals(dummyFavorites, newViewModel.favorites.value)
    }

    @Test
    fun `isFavorite returns correct flow from repository`() = runTest {
        val pokemonId = 1
        every { repository.isFavorite(pokemonId) } returns flowOf(true)
        
        val stateFlow = viewModel.isFavorite(pokemonId)
        
        // Start collecting to trigger WhileSubscribed
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect()
        }
        
        assertTrue(stateFlow.value)
    }

    @Test
    fun `toggleFavorite calls repository toggleFavorite`() = runTest {
        val dummyFavorite = mockk<FavoriteEntity>(relaxed = true)
        val isAlreadyFavorite = false
        
        viewModel.toggleFavorite(dummyFavorite, isAlreadyFavorite)
        
        coVerify { repository.toggleFavorite(dummyFavorite, isAlreadyFavorite) }
    }
}
