package com.tech.pokedex.data.repository

import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.remote.PokeApiService
import com.tech.pokedex.data.remote.model.PokemonDetailResponse
import com.tech.pokedex.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PokemonRepositoryTest {

    private lateinit var apiService: PokeApiService
    private lateinit var database: AppDatabase

    private lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setUp() {
        apiService = mockk()
        database = mockk(relaxed = true)
        pokemonRepository = PokemonRepository(apiService, database)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getPokemonDetail harus mengonversi input nama menjadi lowercase sebelum memanggil API`() = runTest {
        val inputName = "PiKacHu"
        val expectedName = "pikachu"

        val dummyResponse = mockk<PokemonDetailResponse>(relaxed = true)

        coEvery { apiService.getPokemonDetail(expectedName) } returns Response.success(dummyResponse)
        val result = pokemonRepository.getPokemonDetail(inputName)

        assertTrue(result is Resource.Success)
    }

    @Test
    fun `getPokemonDetail mengembalikan Success dengan data yang tepat saat API merespons 200 OK`() = runTest {
        val pokemonName = "bulbasaur"
        val dummyResponse = mockk<PokemonDetailResponse>(relaxed = true)

        coEvery { apiService.getPokemonDetail(pokemonName) } returns Response.success(dummyResponse)
        val result = pokemonRepository.getPokemonDetail(pokemonName)

        assertTrue(result is Resource.Success)
        assertEquals(dummyResponse, (result as Resource.Success).data)
    }

    @Test
    fun `getPokemonDetail mengembalikan Error saat API merespons selain 2xx (contoh 404 Not Found)`() = runTest {
        val pokemonName = "agumon"
        val errorResponseBody = "Not found".toResponseBody(null)

        coEvery { apiService.getPokemonDetail(pokemonName) } returns Response.error(404, errorResponseBody)
        val result = pokemonRepository.getPokemonDetail(pokemonName)

        assertTrue(result is Resource.Error)
        assertEquals("Pokemon tidak ditemukan", (result as Resource.Error).message)
    }

    @Test
    fun `getPokemonDetail menangkap Exception dari Retrofit (misal tidak ada internet) dan mereturn Error`() = runTest {
        val pokemonName = "charmander"
        val expectedErrorMessage = "Timeout connection"

        coEvery { apiService.getPokemonDetail(pokemonName) } throws RuntimeException(expectedErrorMessage)
        val result = pokemonRepository.getPokemonDetail(pokemonName)

        assertTrue(result is Resource.Error)
        assertEquals(expectedErrorMessage, (result as Resource.Error).message)
    }
}