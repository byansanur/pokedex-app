package com.tech.pokedex.ui.viewmodel

import androidx.paging.PagingData
import com.tech.pokedex.data.remote.model.PokemonDetailResponse
import com.tech.pokedex.data.repository.PokemonRepository
import com.tech.pokedex.util.DetailUiState
import com.tech.pokedex.util.MainDispatcherRule
import com.tech.pokedex.util.Resource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PokemonViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: PokemonViewModel
    private lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setUp() {
        pokemonRepository = mockk(relaxed = true)
        every { pokemonRepository.getPokemonPager() } returns flowOf(PagingData.empty())
        viewModel = PokemonViewModel(pokemonRepository)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `pokemonPagingFlow successfully initialized from repository`() {
        val flow = viewModel.pokemonPagingFlow
        assertNotNull(flow)
    }

    @Test
    fun `getPokemonDetail updates detailUiState to Success when repository returns data`() = runTest {
        val pokemonName = "pikachu"
        val dummyResponse = mockk<PokemonDetailResponse>(relaxed = true)
        
        coEvery { pokemonRepository.getPokemonDetail(pokemonName) } returns Resource.Success(dummyResponse)

        viewModel.getPokemonDetail(pokemonName)

        val state = viewModel.detailUiState.value
        assertTrue(state is DetailUiState.Success)
        assertEquals(dummyResponse, (state as DetailUiState.Success).pokemon)
    }

    @Test
    fun `getPokemonDetail updates detailUiState to Error when repository returns error`() = runTest {
        val pokemonName = "unknown"
        val errorMessage = "Pokemon tidak ditemukan"
        
        coEvery { pokemonRepository.getPokemonDetail(pokemonName) } returns Resource.Error(errorMessage)

        viewModel.getPokemonDetail(pokemonName)

        val state = viewModel.detailUiState.value
        assertTrue(state is DetailUiState.Error)
        assertEquals(errorMessage, (state as DetailUiState.Error).message)
    }
}
