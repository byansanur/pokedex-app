package com.tech.pokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tech.pokedex.ui.viewmodel.AuthViewModel
import com.tech.pokedex.ui.viewmodel.PokemonViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            koinViewModel()

            // TODO: Nanti kita ganti dengan LoginScreen yang sesungguhnya
            /*
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    // Jika login sukses, pergi ke Main dan hapus Login dari backstack
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
            */
        }

        composable(Screen.Register.route) {
            koinViewModel()

            /*
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.popBackStack() // Kembali ke Login setelah sukses
                }
            )
            */
        }

        composable(Screen.Main.route) {
            koinViewModel()
            // AuthViewModel juga dibutuhkan di sini untuk tab Profile (Logout)
            koinViewModel()

            /*
            MainScreen(
                pokemonViewModel = pokemonViewModel,
                authViewModel = authViewModel,
                onNavigateToDetail = { pokemonName ->
                    navController.navigate(Screen.Detail.createRoute(pokemonName))
                },
                onLogout = {
                    // Kembali ke Login dan bersihkan semua backstack
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
            */
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("pokemonName") { type = NavType.StringType })
        ) { backStackEntry ->
            //val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""
            // val detailViewModel: DetailViewModel = koinViewModel() // Dibuat nanti

            /*
            DetailScreen(
                pokemonName = pokemonName,
                viewModel = detailViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
            */
        }
    }
}