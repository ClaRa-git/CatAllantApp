package com.cfa.cda.catapp.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cfa.cda.catapp.ui.components.CatBreedImage
import com.cfa.cda.catapp.ui.components.CatLogo
import com.cfa.cda.catapp.ui.components.CharacterBar
import com.cfa.cda.catapp.ui.components.RatingStars
import com.cfa.cda.catapp.ui.theme.LocalCatColors

@Composable
fun BreedDetailScreen(
    breedId: String,
    onBackClick: () -> Unit,
    viewModel: BreedDetailViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val colors = LocalCatColors.current

    LaunchedEffect(breedId) {
        viewModel.loadBreed(breedId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Retour",
                tint=colors.textPrimary,
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onBackClick() }
            )
            Spacer(Modifier.width(10.dp))
            CatLogo()
        }

        Spacer(Modifier.height(14.dp))

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }
            state.errorMessage != null || state.breed == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.errorMessage ?: "Race introuvable", color = colors.accent)
                }
            }
            else -> {
                val breed = state.breed!!

                CatBreedImage(
                    imageId = breed.referenceImageId,
                    contentDescription = breed.name,
                    modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                    imageHeight = 240.dp
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "FICHE RACE",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.primary
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = breed.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = colors.textPrimary
                    )
                    Icon(
                        imageVector = if (state.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favori",
                        tint = if (state.isFavorite) colors.accent else colors.border,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { viewModel.toggleFavorite() }
                    )
                }

                Spacer(Modifier.height(2.dp))

                Text(
                    text = "${(breed.id.hashCode().mod(900) + 100)} vues",
                    fontSize = 11.sp,
                    color = colors.border
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "VOTRE NOTE",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.border
                )
                Spacer(Modifier.height(6.dp))
                RatingStars(
                    rating = state.rating,
                    onRatingChange = { viewModel.setRating(it) }
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = breed.description ?: "",
                    fontSize = 13.sp,
                    color = colors.textSecondary,
                    lineHeight = 20.sp
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InfoCard(
                        label = "ORIGINE",
                        value = breed.origin ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                    InfoCard(
                        label = "POIDS",
                        value = breed.weight?.metric?.let { "$it kg" } ?: "-",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(14.dp))

                Text(
                    text = "CARACTERE",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.border
                )

                Spacer(Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CharacterBar(label = "Affection", value = breed.affectionLevel ?: 0)
                    CharacterBar(label = "Energie", value = breed.energyLevel ?: 0)
                }

                Spacer(Modifier.height(14.dp))

                if (!breed.wikipediaUrl.isNullOrBlank()) {
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(breed.wikipediaUrl))
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primary),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.primary),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Voir sur Wikipedia")
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    val colors = LocalCatColors.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.card),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = colors.border)
            Spacer(Modifier.height(4.dp))
            Text(text = value, fontSize = 14.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Medium)
        }
    }
}