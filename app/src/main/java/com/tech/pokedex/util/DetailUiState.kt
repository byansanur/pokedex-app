package com.tech.pokedex.util

import com.tech.pokedex.data.remote.model.PokemonDetailResponse

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val pokemon: PokemonDetailResponse) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}