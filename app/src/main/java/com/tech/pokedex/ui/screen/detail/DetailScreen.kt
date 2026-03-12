package com.tech.pokedex.ui.screen.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.tech.pokedex.BuildConfig
import com.tech.pokedex.data.remote.model.PokemonDetailResponse
import com.tech.pokedex.ui.theme.PokeDarkBlue
import com.tech.pokedex.ui.theme.PokeYellow
import com.tech.pokedex.ui.viewmodel.FavoriteViewModel
import com.tech.pokedex.ui.viewmodel.PokemonViewModel
import com.tech.pokedex.util.DetailUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    pokemonName: String,
    onNavigateBack: () -> Unit,
    viewModel: PokemonViewModel = koinViewModel(),
    favViewModel: FavoriteViewModel = koinViewModel()
) {
    val uiState by viewModel.detailUiState.collectAsState()

    LaunchedEffect(pokemonName) {
        viewModel.getPokemonDetail(pokemonName)
    }

    // Default Top Bar saat loading atau error
    var topBar: @Composable () -> Unit = {
        DetailTopBar(
            onNavigateBack = onNavigateBack,
            isFavorite = false,
            onFavoriteClick = {}
        )
    }

    if (uiState is DetailUiState.Success) {
        val pokemon = (uiState as DetailUiState.Success).pokemon
        val isFavorite by favViewModel.isFavorite(pokemon.id).collectAsState()

        topBar = {
            DetailTopBar(
                onNavigateBack = onNavigateBack,
                isFavorite = isFavorite,
                onFavoriteClick = {
                    val typesString = pokemon.types.joinToString(",") { it.type.name }
                    favViewModel.toggleFavorite(
                        pokemonId = pokemon.id,
                        name = pokemon.name,
                        types = typesString,
                        isAlreadyFavorite = isFavorite
                    )
                }
            )
        }
    }

    Scaffold(
        topBar = topBar,
        containerColor = Color(0xFFF8F9FA)
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (uiState) {
                is DetailUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PokeYellow
                    )
                }
                is DetailUiState.Error -> {
                    val message = (uiState as DetailUiState.Error).message
                    Text(
                        text = message,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is DetailUiState.Success -> {
                    val pokemon = (uiState as DetailUiState.Success).pokemon

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        PokemonImageHeader(pokemon)
                        Spacer(modifier = Modifier.height(24.dp))
                        PokemonInfoHeader(pokemon)
                        Spacer(modifier = Modifier.height(32.dp))
                        PokemonMetricsRow(pokemon)
                        Spacer(modifier = Modifier.height(32.dp))
                        AbilitiesSection(pokemon)
                        Spacer(modifier = Modifier.height(32.dp))
                        BaseStatsSection(pokemon)
                        Spacer(modifier = Modifier.height(40.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    onNavigateBack: () -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("Pokemon Detail", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PokeDarkBlue) },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back", tint = PokeDarkBlue)
            }
        },
        actions = {
            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else PokeDarkBlue
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
    )
}

@Composable
fun PokemonImageHeader(pokemon: PokemonDetailResponse) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFFF9D2)),
        contentAlignment = Alignment.Center
    ) {
        val imageUrl = "${BuildConfig.IMAGE_URL}${pokemon.id}.png"
        AsyncImage(
            model = imageUrl,
            contentDescription = pokemon.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize().padding(32.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .background(Color.White, RoundedCornerShape(16.dp))
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue
            )
        }
    }
}

@Composable
fun PokemonInfoHeader(pokemon: PokemonDetailResponse) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = pokemon.name.replaceFirstChar { it.uppercase() },
            fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = PokeDarkBlue
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text("Pokemon", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            pokemon.types.forEach { typeSlot ->
                Box(
                    modifier = Modifier
                        .background(PokeYellow, RoundedCornerShape(16.dp))
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = typeSlot.type.name.uppercase(),
                        fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonMetricsRow(pokemon: PokemonDetailResponse) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        MetricCard(title = "WEIGHT", value = "${pokemon.weight / 10.0} kg", modifier = Modifier.weight(1f))
        MetricCard(title = "HEIGHT", value = "${pokemon.height / 10.0} m", modifier = Modifier.weight(1f))
        MetricCard(title = "BASE EXP", value = pokemon.baseExperience.toString(), modifier = Modifier.weight(1f))
    }
}

@Composable
fun MetricCard(title: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = title, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontSize = 16.sp, color = PokeDarkBlue, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SectionTitle(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.width(4.dp).height(20.dp).background(PokeYellow, RoundedCornerShape(50)))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue)
    }
}

@Composable
fun AbilitiesSection(pokemon: PokemonDetailResponse) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "Abilities")
        Spacer(modifier = Modifier.height(16.dp))

        pokemon.abilities.forEach { abilitySlot ->
            AbilityCard(
                name = abilitySlot.ability.name.replaceFirstChar { it.uppercase() },
                badgeText = if (abilitySlot.isHidden) "HIDDEN" else "MAIN",
                description = "Tap to learn more about this ability.",
                badgeColor = if (abilitySlot.isHidden) Color(0xFFFFF9D2) else Color(0xFFEEEEEE),
                badgeTextColor = if (abilitySlot.isHidden) PokeDarkBlue else Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun AbilityCard(name: String, badgeText: String, description: String, badgeColor: Color, badgeTextColor: Color) {
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(16.dp)).padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue)
            Box(modifier = Modifier.background(badgeColor, RoundedCornerShape(8.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                Text(text = badgeText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = badgeTextColor)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = description, fontSize = 14.sp, color = Color.Gray, lineHeight = 20.sp)
    }
}

@Composable
fun BaseStatsSection(pokemon: PokemonDetailResponse) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(title = "Base Stats")
        Spacer(modifier = Modifier.height(16.dp))

        pokemon.stats.forEach { statSlot ->
            val formattedName = when (statSlot.stat.name) {
                "hp" -> "HP"
                "attack" -> "ATK"
                "defense" -> "DEF"
                "special-attack" -> "SP.ATK"
                "special-defense" -> "SP.DEF"
                "speed" -> "SPD"
                else -> statSlot.stat.name.uppercase()
            }
            StatRow(statName = formattedName, statValue = statSlot.baseStat, maxValue = 100)
        }
    }
}

@Composable
fun StatRow(statName: String, statValue: Int, maxValue: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(text = statName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.width(50.dp))
        LinearProgressIndicator(
            progress = { (statValue.toFloat() / 255f).coerceIn(0f, 1f) },
            modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(50)),
            color = PokeYellow, trackColor = Color(0xFFEEEEEE), strokeCap = StrokeCap.Round
        )
        Text(text = statValue.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue, modifier = Modifier.width(32.dp), textAlign = TextAlign.End)
    }
}