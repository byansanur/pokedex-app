package com.tech.pokedex.data.remote

import com.tech.pokedex.data.remote.model.PokemonDetailResponse
import com.tech.pokedex.data.remote.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<PokemonListResponse>


    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String):
            Response<PokemonDetailResponse>
}