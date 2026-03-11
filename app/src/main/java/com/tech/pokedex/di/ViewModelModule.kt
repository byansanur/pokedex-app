package com.tech.pokedex.di

import com.tech.pokedex.ui.viewmodel.AuthViewModel
import com.tech.pokedex.ui.viewmodel.PokemonViewModel
import com.tech.pokedex.ui.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { PokemonViewModel(get())  }
    viewModel { SearchViewModel(get()) }
}