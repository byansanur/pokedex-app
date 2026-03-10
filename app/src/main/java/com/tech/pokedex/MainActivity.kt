package com.tech.pokedex

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.tech.pokedex.data.local.SessionManager
import com.tech.pokedex.ui.navigation.AppNavHost
import com.tech.pokedex.ui.navigation.Screen
import com.tech.pokedex.ui.theme.PokedexTheme
import com.tech.pokedex.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : FragmentActivity() {
    private val authViewModel: AuthViewModel by viewModel()
    private val sessionManager: SessionManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                lifecycleScope.launch {
                    sessionManager.isSessionExpired.collect { expired ->
                        Log.d("AUTH_TEST", "Is Session Expired: $expired")
                        if (expired) {
                            sessionManager.clearSession()
                        } else {
                            sessionManager.updateLastActiveTime()
                        }
                    }
                }
            }
        })

        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
                    val startDestination = if (isLoggedIn) Screen.Main.route else Screen.Login.route

                    val navController = rememberNavController()
                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination,
                        sessionManager = sessionManager
                    )
                }
            }
        }
    }
}
