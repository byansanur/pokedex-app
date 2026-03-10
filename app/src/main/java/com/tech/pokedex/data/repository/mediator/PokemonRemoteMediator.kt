package com.tech.pokedex.data.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tech.pokedex.data.local.AppDatabase
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.remote.PokeApiService
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val apiService: PokeApiService,
    private val database: AppDatabase
) : RemoteMediator<Int, PokemonEntity>() {

    private val pokeDao = database.pokemonDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val count = pokeDao.getPokemonCount()
                    if (count == 0) return MediatorResult.Success(endOfPaginationReached = false)
                    count
                }
            }

            val response = apiService.getPokemonList(
                limit = state.config.pageSize,
                offset = offset
            )

            if (response.isSuccessful && response.body() != null) {
                val apiResult = response.body()!!.results
                val endOfPaginationReached = apiResult.isEmpty()

                database.withTransaction {
                    if (loadType == LoadType.REFRESH) {
                        pokeDao.clearAll()
                    }

                    val entities = apiResult.map { item ->
                        val pokemonId = item.url.trimEnd('/').split('/').last().toInt()
                        PokemonEntity(
                            id = pokemonId,
                            name = item.name,
                            url = item.url
                        )
                    }
                    pokeDao.insertAll(entities)
                }
                MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } else {
                MediatorResult.Error(Exception("API request failed"))
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}