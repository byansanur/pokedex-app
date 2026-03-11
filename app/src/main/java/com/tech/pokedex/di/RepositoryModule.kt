package com.tech.pokedex.di

import com.tech.pokedex.data.repository.PokemonRepository
import com.tech.pokedex.data.repository.SearchRepository
import com.tech.pokedex.data.repository.UserRepository
import org.koin.dsl.module

val repositoryModule = module {
    single { UserRepository(get(), get()) }
    single { PokemonRepository(get(), get()) }
    single { SearchRepository(get()) }
}