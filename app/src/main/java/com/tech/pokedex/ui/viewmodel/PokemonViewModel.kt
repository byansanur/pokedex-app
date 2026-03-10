package com.tech.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

class PokemonViewModel(
    private val pokemonRepository: PokemonRepository
) : ViewModel() {

    val pokemonPagingFlow: Flow<PagingData<PokemonEntity>> =
        pokemonRepository.getPokemonPager().cachedIn(viewModelScope)
}