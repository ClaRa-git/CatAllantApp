package com.cfa.cda.catapp.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cfa.cda.catapp.ui.components.BreedCard
import com.cfa.cda.catapp.ui.components.CatLogo
import com.cfa.cda.catapp.ui.theme.LocalCatColors

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel(),
    onBreedClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val colors = LocalCatColors.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadFavorites()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
    ) {
        CatLogo()

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Vos favoris",
            style = MaterialTheme.typography.headlineMedium,
            color = colors.textPrimary
        )

        Spacer(Modifier.height(12.dp))

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
            state.favorites.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.HeartBroken,
                            contentDescription = null,
                            tint = colors.border,
                            modifier = Modifier.height(40.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Aucun favori pour le moment",
                            color = colors.textSecondary
                        )
                    }
                }
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(state.favorites, key = { it.breedId }) { fav ->
                        BreedCard(
                            name = fav.name,
                            imageId = fav.imageId,
                            ratingLabel = if (fav.rating > 0) "★ ${fav.rating}" else "Pas encore note",
                            isFavorite = true,
                            onClick = { onBreedClick(fav.breedId) }
                        )
                    }
                }
            }
        }
    }
}