package com.tech.pokedex.ui.auth

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Rectangle
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalAutofillManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.FragmentActivity
import com.tech.pokedex.ui.components.PasswordTextField
import com.tech.pokedex.ui.components.PokedexTextField
import com.tech.pokedex.ui.components.PrimaryAuthButton
import com.tech.pokedex.ui.theme.PokeDarkBlue
import com.tech.pokedex.ui.theme.PokeYellow
import com.tech.pokedex.ui.viewmodel.AuthViewModel
import com.tech.pokedex.util.BiometricUtil
import com.tech.pokedex.util.Resource

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val isBiometricEnabled by viewModel.isBiometricEnabledForLastUser.collectAsState()
    val lastLoggedInUserId by viewModel.lastLoggedInUserId.collectAsState()

    var trainerId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val headerHeight = 320.dp

    val autofillManager = LocalAutofillManager.current

    LaunchedEffect(authState) {
        when (val state = authState) {
            is Resource.Success -> {
                viewModel.resetAuthState()
                onLoginSuccess()
            }
            is Resource.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        val screenHeight = maxHeight
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(PokeYellow)
                .padding(top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Bolt, contentDescription = "Logo", tint = PokeDarkBlue, modifier = Modifier.size(50.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Pika-Hello!", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = PokeDarkBlue)
            Text("Welcome back, Master Trainer", fontSize = 16.sp, color = PokeDarkBlue)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
        ) {
            Spacer(modifier = Modifier.height(headerHeight - 40.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = screenHeight - (headerHeight - 40.dp)),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .padding(top = 32.dp)
                        .navigationBarsPadding()
                        .padding(bottom = 24.dp)
                ) {
                    Text("Login", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue)
                    Text("Enter your credentials to access your Pokedex", color = Color.Gray, modifier = Modifier.padding(bottom = 24.dp))

                    Text("Trainer ID", fontWeight = FontWeight.Medium, color = PokeDarkBlue, modifier = Modifier.padding(bottom = 8.dp))
                    PokedexTextField(
                        value = trainerId,
                        onValueChange = { trainerId = it },
                        label = "AshKetchum_01",
                        leadingIcon = Icons.Filled.Person,
                        isNewUser = false
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Password", fontWeight = FontWeight.Medium, color = PokeDarkBlue, modifier = Modifier.padding(bottom = 8.dp))
                    PasswordTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "••••••••",
                        leadingIcon = Icons.Filled.Lock,
                        isNewUser = false
                    )

                    Text(
                        text = "Forgot Password?",
                        color = PokeDarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 24.dp, end = 4.dp),
                        textAlign = TextAlign.End
                    )


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PrimaryAuthButton(
                            text = "BATTLE START",
                            isLoading = authState is Resource.Loading,
                            icon = Icons.Outlined.Rectangle,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                if (trainerId.isNotBlank() && password.isNotBlank()) {
                                    autofillManager?.commit()
                                    viewModel.login(trainerId, password)
                                } else {
                                    Toast.makeText(context, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )

                        if (isBiometricEnabled && lastLoggedInUserId != null) {
                            OutlinedButton(
                                onClick = {
                                    BiometricUtil.showBiometricPrompt(context as FragmentActivity) {
                                        viewModel.loginByBiometric(lastLoggedInUserId!!)
                                    }
                                },
                                modifier = Modifier.size(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(2.dp, PokeDarkBlue.copy(alpha = 0.5f)),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Fingerprint,
                                    contentDescription = "Login with Biometric",
                                    tint = PokeDarkBlue,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f, fill = false))
                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("New Trainer? ", color = Color.Gray)
                        Text(
                            "Register Now",
                            color = PokeDarkBlue,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onNavigateToRegister
                            )
                        )
                    }
                }
            }
        }
    }
}