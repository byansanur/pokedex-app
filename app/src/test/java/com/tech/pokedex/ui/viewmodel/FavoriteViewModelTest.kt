package com.tech.pokedex.ui.viewmodel

import com.tech.pokedex.data.local.SessionManager
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
    private lateinit var sessionManager: SessionManager
    private val userId = "user123"

    @Before
    fun setUp() {
        repository = mockk(relaxed = true)
        sessionManager = mockk(relaxed = true)
        
        every { sessionManager.activeUserId } returns flowOf(userId)
        every { repository.getFavorites(userId) } returns flowOf(emptyList())
        
        viewModel = FavoriteViewModel(repository, sessionManager)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `favorites state initialized from repository for active user`() = runTest {
        val dummyFavorites = listOf(mockk<FavoriteEntity>(relaxed = true))
        every { repository.getFavorites(userId) } returns flowOf(dummyFavorites)
        
        val newViewModel = FavoriteViewModel(repository, sessionManager)
        
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            newViewModel.favorites.collect()
        }
        
        assertEquals(dummyFavorites, newViewModel.favorites.value)
    }

    @Test
    fun `isFavorite returns correct flow from repository for active user`() = runTest {
        val pokemonId = 1
        every { repository.isFavorite(pokemonId, userId) } returns flowOf(true)
        
        val stateFlow = viewModel.isFavorite(pokemonId)
        
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            stateFlow.collect()
        }
        
        assertTrue(stateFlow.value)
    }

    @Test
    fun `toggleFavorite calls repository toggleFavorite with active userId`() = runTest {
        val pokemonId = 1
        val name = "Pikachu"
        val types = "Electric"
        val isAlreadyFavorite = false
        
        every { sessionManager.activeUserId } returns flowOf(userId)

        viewModel.toggleFavorite(pokemonId, name, types, isAlreadyFavorite)
        
        coVerify { 
            repository.toggleFavorite(
                match { it.id == pokemonId && it.userId == userId && it.name == name },
                isAlreadyFavorite 
            ) 
        }
    }

    @Test
    fun `favorites state is empty when no user is logged in`() = runTest {
        every { sessionManager.activeUserId } returns flowOf(null)

        val newViewModel = FavoriteViewModel(repository, sessionManager)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            newViewModel.favorites.collect()
        }

        assertTrue(newViewModel.favorites.value.isEmpty())
    }
}
