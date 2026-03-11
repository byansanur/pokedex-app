package com.tech.pokedex.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.data.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: SearchRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<PokemonEntity>>(emptyList())
    val searchResults: StateFlow<List<PokemonEntity>> = _searchResults.asStateFlow()

    val recentSearches = repository.getRecentSearches()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        // Menjalankan observasi aliran data saat ViewModel dibuat
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(500L)
                .distinctUntilChanged()
                .collect { query ->
                    if (query.length > 2) {
                        val results = repository.searchPokemonLocal(query)
                        _searchResults.value = results
                    } else {
                        _searchResults.value = emptyList()
                    }
                }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun executeSearch(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.value = emptyList()
                return@launch
            }
            val results = repository.searchPokemonLocal(query)
            _searchResults.value = results
        }
    }

    fun saveSearchQuery(query: String) {
        if (query.isNotBlank()) {
            viewModelScope.launch {
                repository.insertSearchQuery(query)
            }
        }
    }

    fun deleteRecentSearch(query: String) {
        viewModelScope.launch {
            repository.deleteSearchQuery(query)
        }
    }

    fun clearAllRecentSearches() {
        viewModelScope.launch {
            repository.clearAllHistory()
        }
    }
}