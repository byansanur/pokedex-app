package com.tech.pokedex.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.CatchingPokemon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.tech.pokedex.BuildConfig
import com.tech.pokedex.data.local.entity.PokemonEntity
import com.tech.pokedex.ui.theme.PokeDarkBlue
import com.tech.pokedex.ui.theme.PokeYellow
import com.tech.pokedex.ui.viewmodel.PokemonViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    onNavigateToDetail: (String) -> Unit,
    viewModel: PokemonViewModel = koinViewModel()
) {
    val pokemonList = viewModel.pokemonPagingFlow.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HomeHeader()

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 80.dp),// Beri jarak bawah untuk Bottom Nav
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                FeaturedPokemonCard(onCatchClick = { onNavigateToDetail("pikachu") })
            }

            items(
                count = pokemonList.itemCount,
                key = pokemonList.itemKey { it.name },
                contentType = pokemonList.itemContentType { "PokemonCard" }
            ) { index ->
                val pokemon = pokemonList[index]
                if (pokemon != null) {
                    PokemonCard(
                        pokemon = pokemon,
                        onClick = { onNavigateToDetail(pokemon.name) }
                    )
                }
            }

            pokemonList.apply {
                when {
                    loadState.append is LoadState.Loading -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = PokeDarkBlue)
                            }
                        }
                    }
                    loadState.append is LoadState.Error -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Text(
                                text = "Gagal memuat data",
                                color = Color.Red,
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    loadState.refresh is LoadState.Loading -> {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = PokeDarkBlue)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun PokemonCard(
    pokemon: PokemonEntity,
    onClick: () -> Unit
) {
    val imageUrl = "${BuildConfig.IMAGE_URL}${pokemon.id}.png"
    val context = LocalContext.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .size(300)
                    .build(),
                contentDescription = pokemon.name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 12.dp)
            )

            Text(
                text = "#${pokemon.id.toString().padStart(3, '0')}",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = pokemon.name.replaceFirstChar { it.uppercase() },
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = PokeDarkBlue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PokeYellow),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.CatchingPokemon, contentDescription = "Logo", tint = PokeDarkBlue)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("Pokedex", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = PokeDarkBlue)
        }

        IconButton(
            onClick = { /* TODO: Notifikasi */ },
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(12.dp))
                .size(40.dp)
        ) {
            Icon(Icons.Filled.Notifications, contentDescription = "Notifications", tint = PokeDarkBlue)
        }
    }
}

@Composable
fun FeaturedPokemonCard(onCatchClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(PokeYellow)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp, top = 24.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("POKEMON OF THE DAY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Pikachu", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = PokeDarkBlue)
                    Spacer(modifier = Modifier.height(4.dp))
                    // Badge Tipe
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.5f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("ELECTRIC", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = PokeDarkBlue)
                    }
                }

                Button(
                    onClick = onCatchClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PokeDarkBlue, contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text("Catch Now", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(150.dp)
                    .background(PokeDarkBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.CatchingPokemon, contentDescription = null, modifier = Modifier.size(80.dp), tint = PokeYellow)
            }
        }
    }
}