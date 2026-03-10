package com.tech.pokedex.ui.viewmodel

import androidx.paging.PagingData
import com.tech.pokedex.data.repository.PokemonRepository
import com.tech.pokedex.util.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Assert.assertNotNull
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
}