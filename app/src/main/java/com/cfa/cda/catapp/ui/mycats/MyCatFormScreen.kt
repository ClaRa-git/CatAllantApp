package com.cfa.cda.catapp.ui.mycats

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cfa.cda.catapp.ui.components.CatLogo
import com.cfa.cda.catapp.ui.theme.LocalCatColors
import androidx.compose.material3.OutlinedTextFieldDefaults

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MyCatFormScreen(
    catId: Long,
    onBackClick: () -> Unit,
    viewModel: MyCatFormViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val colors = LocalCatColors.current

    LaunchedEffect(catId) {
        viewModel.loadCat(catId)
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onBackClick()
    }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onPhotoSelected(it.toString()) }
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
                modifier = Modifier
                    .size(22.dp)
                    .clickable { onBackClick() }
            )
            Spacer(Modifier.width(10.dp))
            CatLogo()
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = if (state.id == 0L) "Ajouter un chat" else "Modifier ${state.name}",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        // Photo
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(colors.imagePlaceholder)
                .clickable {
                    photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            contentAlignment = Alignment.Center
        ) {
            if (state.photoUri != null) {
                AsyncImage(
                    model = state.photoUri,
                    contentDescription = "Photo du chat",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp).clip(CircleShape)
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Pets,
                        contentDescription = null,
                        tint = colors.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(32.dp)
                    )
                    Text("Photo", fontSize = 10.sp, color = colors.textSecondary)
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // Nom
        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = { Text("Nom du chat") },
            singleLine = true,
            colors = catTextFieldColors(colors),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        // Race - dropdown
        var breedMenuExpanded by remember { mutableStateOf(false) }

        if (!state.useCustomBreed) {
            ExposedDropdownMenuBox(
                expanded = breedMenuExpanded,
                onExpandedChange = { breedMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = state.selectedBreed?.name ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Race") },
                    placeholder = { Text("Selectionner une race") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = breedMenuExpanded) },
                    colors = catTextFieldColors(colors),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                )
                ExposedDropdownMenuBox(
                    expanded = breedMenuExpanded,
                    onExpandedChange = { breedMenuExpanded = it }
                ) {
                    OutlinedTextField(
                        value = state.selectedBreed?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Race") },
                        placeholder = { Text("Selectionner une race") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = breedMenuExpanded) },
                        colors = catTextFieldColors(colors),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                    )
                    ExposedDropdownMenu(
                        expanded = breedMenuExpanded,
                        onDismissRequest = { breedMenuExpanded = false }
                    ) {
                        state.allBreeds.forEach { breed ->
                            DropdownMenuItem(
                                text = { Text(breed.name) },
                                onClick = {
                                    viewModel.onBreedSelected(breed)
                                    breedMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = state.useCustomBreed,
                onCheckedChange = { viewModel.onUseCustomBreedChange(it) },
                colors = CheckboxDefaults.colors(checkedColor = colors.primary)
            )
            Text(
                text = "Race non listee / personnalisee",
                fontSize = 13.sp,
                color = colors.textSecondary
            )
        }

        if (state.useCustomBreed) {
            Spacer(Modifier.height(4.dp))
            OutlinedTextField(
                value = state.customBreedName,
                onValueChange = { viewModel.onCustomBreedNameChange(it) },
                label = { Text("Nom de la race") },
                singleLine = true,
                colors = catTextFieldColors(colors),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = state.age,
                    onValueChange = { viewModel.onAgeChange(it) },
                    label = { Text("Age") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = catTextFieldColors(colors),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AgeUnit.entries.forEach { unit ->
                        val selected = state.ageUnit == unit
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (selected) colors.primary else colors.card)
                                .clickable { viewModel.onAgeUnitChange(unit) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = unit.label,
                                fontSize = 12.sp,
                                color = if (selected) colors.card else colors.textSecondary
                            )
                        }
                    }
                }
            }
            OutlinedTextField(
                value = state.weight,
                onValueChange = { viewModel.onWeightChange(it) },
                label = { Text("Poids (kg)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = catTextFieldColors(colors),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.notes,
            onValueChange = { viewModel.onNotesChange(it) },
            label = { Text("Notes") },
            minLines = 3,
            colors = catTextFieldColors(colors),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { viewModel.save() },
            enabled = state.name.isNotBlank(),
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.id == 0L) "Ajouter" else "Enregistrer")
        }

        if (state.id != 0L) {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { viewModel.delete() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.accent),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, colors.accent),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Supprimer")
            }
        }

        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
private fun catTextFieldColors(colors: com.cfa.cda.catapp.ui.theme.CatColorScheme) =
    OutlinedTextFieldDefaults.colors(
        focusedContainerColor = colors.card,
        unfocusedContainerColor = colors.card,
        focusedBorderColor = colors.primary,
        unfocusedBorderColor = colors.border,
        focusedLabelColor = colors.primary,
        unfocusedLabelColor = colors.textSecondary
    )