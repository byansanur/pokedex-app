package com.tech.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.repository.PokemonRepository
import com.tech.pokedex.util.DetailUiState
import com.tech.pokedex.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState = _detailUiState.asStateFlow()

    val pokemonPagingFlow: Flow<PagingData<PokemonEntity>> =
        pokemonRepository.getPokemonPager().cachedIn(viewModelScope)

    fun getPokemonDetail(name: String) {
        _detailUiState.value = DetailUiState.Loading
        viewModelScope.launch {
            when (val result = pokemonRepository.getPokemonDetail(name)) {
                is Resource.Success -> {
                    // Handle sukses
                    _detailUiState.value = DetailUiState.Success(result.data)
                }
                is Resource.Error -> {
                    // Handle error
                    _detailUiState.value = DetailUiState.Error(result.message)
                }
                else -> {}
            }
        }
    }
}