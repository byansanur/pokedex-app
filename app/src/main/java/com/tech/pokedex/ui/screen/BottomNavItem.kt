package com.tech.pokedex.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("Home", Icons.Filled.Home)
    object Favorites : BottomNavItem("Search", Icons.Filled.Search)
    object Profile : BottomNavItem("Profile", Icons.Filled.Person)
}