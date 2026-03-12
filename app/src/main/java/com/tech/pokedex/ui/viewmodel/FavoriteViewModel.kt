package com.tech.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.pokedex.data.local.SessionManager
import com.tech.pokedex.data.local.entity.FavoriteEntity
import com.tech.pokedex.data.repository.FavoriteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repository: FavoriteRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val favorites: StateFlow<List<FavoriteEntity>> = sessionManager.activeUserId
        .flatMapLatest {
            if (it != null) repository.getFavorites(it)
            else flowOf(emptyList())
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    fun isFavorite(pokemonId: Int): StateFlow<Boolean> = sessionManager.activeUserId
        .flatMapLatest { userId ->
            if (userId != null) {
                repository.isFavorite(pokemonId, userId)
            } else {
                flowOf(false)
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    fun toggleFavorite(pokemonId: Int, name: String, types: String, isAlreadyFavorite: Boolean) {
        viewModelScope.launch {
            val currentUserId = sessionManager.activeUserId.firstOrNull() ?: return@launch

            val entity = FavoriteEntity(
                id = pokemonId,
                userId = currentUserId,
                name = name,
                types = types
            )

            repository.toggleFavorite(entity, isAlreadyFavorite)
        }
    }

}