package com.tech.pokedex.ui.auth

import android.content.ClipData
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CatchingPokemon
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.tech.pokedex.ui.components.PasswordTextField
import com.tech.pokedex.ui.components.PokedexTextField
import com.tech.pokedex.ui.components.PrimaryAuthButton
import com.tech.pokedex.ui.theme.PokeDarkBlue
import com.tech.pokedex.ui.theme.PokeYellow
import com.tech.pokedex.ui.viewmodel.AuthViewModel
import com.tech.pokedex.util.Resource
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var generatedTrainerId by remember { mutableStateOf("") }

    val headerHeight = 320.dp

    LaunchedEffect(authState) {
        when (val state = authState) {
            is Resource.Success -> {
                generatedTrainerId = state.data.trainerId
                showDialog = true
                viewModel.resetAuthState()
            }
            is Resource.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
            }
            else -> Unit
        }
    }

    if (showDialog) {
        SuccessRegisterDialog(
            trainerId = generatedTrainerId,
            onContinueClick = {
                showDialog = false
                onRegisterSuccess()
            }
        )
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
                .padding(top = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back", tint = PokeDarkBlue)
                }
            }

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = "Register Logo", tint = PokeDarkBlue, modifier = Modifier.size(45.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Join the Journey", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = PokeDarkBlue)
            Text("Create your trainer profile", fontSize = 16.sp, color = PokeDarkBlue)
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
                    Text("Register", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue)
                    Text("Enter your details to get started", color = Color.Gray, modifier = Modifier.padding(bottom = 24.dp))

                    Text("Full Name", fontWeight = FontWeight.Medium, color = PokeDarkBlue, modifier = Modifier.padding(bottom = 8.dp))
                    PokedexTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Ash Ketchum",
                        leadingIcon = Icons.Filled.Person,
                        isNewUser = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Password", fontWeight = FontWeight.Medium, color = PokeDarkBlue, modifier = Modifier.padding(bottom = 8.dp))
                    PasswordTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "••••••••",
                        leadingIcon = Icons.Filled.Lock,
                        isNewUser = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    PrimaryAuthButton(
                        text = "CREATE ACCOUNT",
                        isLoading = authState is Resource.Loading,
                        icon = Icons.Filled.CatchingPokemon,
                        onClick = {
                            if (fullName.isNotBlank() && password.isNotBlank()) {
                                viewModel.register(fullName, password)
                            } else {
                                Toast.makeText(context, "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f, fill = false))
                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Already have an account? ", color = Color.Gray)
                        Text(
                            "Login",
                            color = PokeDarkBlue,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onNavigateBack
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessRegisterDialog(
    trainerId: String,
    onContinueClick: () -> Unit
) {
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = { /* Jangan ditutup dengan klik luar */ }) {
        Box(
            modifier = Modifier
                .width(320.dp)
                .background(Color.White, RoundedCornerShape(24.dp))
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(PokeYellow, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Registration Successful!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue, textAlign = TextAlign.Center)
                    Text("Welcome to the world of Pokemon. Your trainer profile is now active. Use Trainer ID for Login.", fontSize = 12.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp, bottom = 24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(2.dp, PokeYellow.copy(alpha = 0.5f)), RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFFDE7), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text("NEW TRAINER ID", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(trainerId, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = PokeDarkBlue)
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            val clipData = ClipData.newPlainText(trainerId, trainerId)
                                            clipboardManager.setClipEntry(clipData.toClipEntry())
                                        }
                                    },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(PokeYellow, RoundedCornerShape(8.dp))
                                ) {
                                    Icon(Icons.Filled.ContentCopy, contentDescription = "Copy", tint = PokeDarkBlue, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = onContinueClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PokeYellow, contentColor = PokeDarkBlue),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Continue to Login", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Download Trainer Card", fontSize = 12.sp, color = Color.Gray)
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = 65.dp)
                    .size(70.dp)
                    .graphicsLayer {
                        shadowElevation = 8.dp.toPx()
                        shape = CircleShape
                        clip = true
                    }
                    .background(Color.White, CircleShape)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xFF4CAF50), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Success", tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}