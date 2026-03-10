package com.tech.pokedex.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.remote.PokeApiService
import com.tech.pokedex.data.remote.model.PokemonDetailResponse
import com.tech.pokedex.data.repository.mediator.PokemonRemoteMediator
import com.tech.pokedex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PokemonRepository(
    private val apiService: PokeApiService,
    private val database: AppDatabase
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getPokemonPager(): Flow<PagingData<PokemonEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,       // Ambil 10 data per halaman (sesuai requirement)
                prefetchDistance = 2 // Trigger load selanjutnya saat tersisa 2 item di bawah
            ),
            remoteMediator = PokemonRemoteMediator(
                apiService = apiService,
                database = database
            ),
            pagingSourceFactory = { database.pokemonDao().getPokemonsPagingSource() }
        ).flow
    }

    // Requirement 3 & 4: Detail & Search
    suspend fun getPokemonDetail(name: String): Resource<PokemonDetailResponse> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getPokemonDetail(name.lowercase())
            if (response.isSuccessful && response.body() != null) {
                Resource.Success(response.body()!!)
            } else {
                Resource.Error("Pokemon tidak ditemukan")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Terjadi kesalahan jaringan")
        }
    }
}