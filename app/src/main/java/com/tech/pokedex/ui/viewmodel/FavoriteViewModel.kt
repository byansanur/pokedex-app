package com.tech.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.pokedex.data.local.entity.FavoriteEntity
import com.tech.pokedex.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: FavoriteRepository
) : ViewModel() {

    val favorites: StateFlow<List<FavoriteEntity>> = repository.getFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun isFavorite(id: Int): StateFlow<Boolean> = repository.isFavorite(id)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun toggleFavorite(pokemon: FavoriteEntity, isAlreadyFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(pokemon, isAlreadyFavorite)
        }
    }

}