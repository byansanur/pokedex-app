package com.tech.pokedex.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tech.pokedex.data.local.SessionManager
import com.tech.pokedex.ui.auth.LoginScreen
import com.tech.pokedex.ui.auth.RegisterScreen
import com.tech.pokedex.ui.viewmodel.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    sessionManager: SessionManager,
    modifier: Modifier = Modifier
) {
    val isLoggedIn by sessionManager.isLoggedIn.collectAsState(initial = null)
    val isExpired by sessionManager.isSessionExpired.collectAsState(initial = false)

    LaunchedEffect(isLoggedIn, isExpired) {
        if (isLoggedIn == false || isExpired) {
            navController.navigate(Screen.Login.route) {
                // Bersihkan semua tumpukan layar agar user tidak bisa tekan 'Back'
                popUpTo(0) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            val authViewModel: AuthViewModel = koinViewModel()
            LoginScreen(
                viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            val authViewModel: AuthViewModel = koinViewModel()
            RegisterScreen(
                viewModel = authViewModel,
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.popBackStack() // Kembali ke Login setelah sukses
                }
            )
        }

        composable(Screen.Main.route) {

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