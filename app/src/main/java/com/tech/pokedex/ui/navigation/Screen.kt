package com.tech.pokedex.ui.navigation

sealed class Screen(val route: String) {
    object Login: Screen("login")
    object Register: Screen("register")
    object Main: Screen("main")
    object Detail: Screen("detail/{pokemonName}") {
        fun createRoute(pokemonName: String) = "detao/$pokemonName"
    }
}