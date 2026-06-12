package com.cfa.cda.catapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cfa.cda.catapp.ui.components.CatBreedImage
import com.cfa.cda.catapp.ui.components.CatLogo
import com.cfa.cda.catapp.ui.theme.LocalCatColors
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onBreedClick: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val colors = LocalCatColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp)
    ) {
        CatLogo()

        Spacer(Modifier.height(16.dp))

        Text(
            text = "DECOUVRIR",
            style = MaterialTheme.typography.labelSmall,
            color = colors.primary
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = buildAnnotatedString {
                append("Une race au hasard, ")
                withStyle(SpanStyle(color = colors.primary, fontStyle = FontStyle.Italic)) {
                    append("a chaque clic")
                }
            },
            style = MaterialTheme.typography.headlineMedium,
            color = colors.textPrimary
        )

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.card),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = state.breed != null) {
                            state.breed?.let { onBreedClick(it.id) }
                        },
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CatBreedImage(
                        imageId = state.breed?.referenceImageId,
                        contentDescription = state.breed?.name,
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = state.breed?.name ?: "...",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = if (state.rating > 0) "★ ${state.rating} — favori" else "Pas encore note",
                                fontSize = 12.sp,
                                color = colors.textSecondary
                            )
                        }
                        Icon(
                            imageVector = if (state.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favori",
                            tint = if (state.isFavorite) colors.accent else colors.border,
                            modifier = Modifier
                                .size(22.dp)
                                .clickable { viewModel.toggleFavorite() }
                        )
                    }
                }

                Button(
                    onClick = { viewModel.loadRandomBreed() },
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.height(16.dp)
                    )
                    Text(
                        text = "  Une autre race",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        if (state.errorMessage != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = state.errorMessage ?: "",
                color = colors.accent,
                fontSize = 12.sp
            )
        }
    }
}