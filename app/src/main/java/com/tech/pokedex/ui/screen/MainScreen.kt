package com.tech.pokedex.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tech.pokedex.ui.screen.favorite.FavoritesScreen
import com.tech.pokedex.ui.screen.home.HomeScreen
import com.tech.pokedex.ui.screen.profile.ProfileScreen
import com.tech.pokedex.ui.theme.PokeYellow


@Composable
fun MainScreen(
    onNavigateToDetail: (String) -> Unit,
    onLogout: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color.Gray,
                tonalElevation = 8.dp
            ) {
                tabs.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                        label = { Text(text = item.title, fontSize = 10.sp, fontWeight = FontWeight.SemiBold) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = PokeYellow,
                            selectedTextColor = PokeYellow,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA))
        ) {
            when (selectedTab) {
                0 -> HomeScreen(
                    onNavigateToDetail = { pokemonName ->
                        onNavigateToDetail(pokemonName)
                    }
                )
                1 -> FavoritesScreen(
                    onNavigateBack = { selectedTab = 0 }, // Kembali ke Home
                    onNavigateToDetail = { pokemonName ->
                        onNavigateToDetail(pokemonName)
                    }
                )
                2 -> ProfileScreen(
                    onLogout = onLogout,
                    onBackClick = { selectedTab = 0 }
                )
            }
        }
    }
}