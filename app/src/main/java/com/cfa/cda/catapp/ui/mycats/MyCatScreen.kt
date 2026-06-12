package com.cfa.cda.catapp.ui.mycats

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cfa.cda.catapp.data.db.MyCat
import com.cfa.cda.catapp.ui.components.CatLogo
import com.cfa.cda.catapp.ui.theme.LocalCatColors

@Composable
fun MyCatsScreen(
    viewModel: MyCatsViewModel = viewModel(),
    onAddClick: () -> Unit,
    onCatClick: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val colors = LocalCatColors.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadCats()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = colors.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = colors.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un chat", tint = colors.card)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .padding(padding)
                .padding(16.dp)
        ) {
            CatLogo()

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Mes chats",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(12.dp))

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }
                state.cats.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Outlined.Pets,
                                contentDescription = null,
                                tint = colors.border,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Aucun chat ajouté pour le moment",
                                color = colors.textSecondary
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "Appuyez sur + pour ajouter votre chat",
                                color = colors.textSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(state.cats, key = { it.id }) { cat ->
                            MyCatCard(cat = cat, onClick = { onCatClick(cat.id) })
                        }
                        item { Spacer(Modifier.height(72.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun MyCatCard(cat: MyCat, onClick: () -> Unit) {
    val colors = LocalCatColors.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.card),
        elevation = CardDefaults.cardElevation(1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(colors.imagePlaceholder),
                contentAlignment = Alignment.Center
            ) {
                if (cat.photoUri != null) {
                    AsyncImage(
                        model = cat.photoUri,
                        contentDescription = cat.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(58.dp).clip(CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Pets,
                        contentDescription = cat.name,
                        tint = colors.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(text = cat.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = cat.customBreedName ?: cat.breedId ?: "Race non renseignee",
                    fontSize = 12.sp,
                    color = colors.textSecondary,
                    modifier = Modifier.padding(top = 3.dp)
                )
                if (cat.age != null || cat.weight != null) {
                    Row {
                        cat.age?.let { months ->
                            val text = if (months >= 12 && months % 12 == 0) {
                                "${months / 12} an${if (months / 12 > 1) "s" else ""}"
                            } else {
                                "$months mois"
                            }
                            Text(text = text, fontSize = 11.sp, color = colors.border)
                        }
                        if (cat.age != null && cat.weight != null) {
                            Text(text = " · ", fontSize = 11.sp, color = colors.border)
                        }
                        cat.weight?.let {
                            Text(text = "$it kg", fontSize = 11.sp, color = colors.border)
                        }
                    }
                }
            }
        }
    }
}