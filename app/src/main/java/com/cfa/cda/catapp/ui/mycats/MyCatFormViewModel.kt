package com.cfa.cda.catapp.ui.mycats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cfa.cda.catapp.data.db.MyCat
import com.cfa.cda.catapp.data.model.Breed
import com.cfa.cda.catapp.data.repository.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AgeUnit(val label: String) {
    MONTHS("mois"),
    YEARS("ans")
}

data class MyCatFormUiState(
    val id: Long = 0,
    val name: String = "",
    val selectedBreed: Breed? = null,
    val customBreedName: String = "",
    val useCustomBreed: Boolean = false,
    val photoUri: String? = null,
    val age: String = "",
    val ageUnit: AgeUnit = AgeUnit.MONTHS,
    val weight: String = "",
    val notes: String = "",
    val allBreeds: List<Breed> = emptyList(),
    val isLoading: Boolean = false,
    val isSaved: Boolean = false
)

class MyCatFormViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CatRepository(application)

    private val _uiState = MutableStateFlow(MyCatFormUiState())
    val uiState: StateFlow<MyCatFormUiState> = _uiState

    init {
        loadBreeds()
    }

    private fun loadBreeds() {
        viewModelScope.launch {
            try {
                val breeds = repository.getAllBreeds().sortedBy { it.name }
                _uiState.update { it.copy(allBreeds = breeds) }
            } catch (e: Exception) {
                // liste vide en cas d'erreur, l'utilisateur pourra utiliser une race personnalisee
            }
        }
    }

    fun loadCat(catId: Long) {
        if (catId == 0L) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cat = repository.getMyCatById(catId)
            if (cat != null) {
                val breeds = _uiState.value.allBreeds.ifEmpty { repository.getAllBreeds() }
                val matchedBreed = breeds.find { it.id == cat.breedId }

                // Affichage : si >= 12 mois et multiple de 12, on propose en annees
                val ageInMonths = cat.age
                val (displayAge, unit) = if (ageInMonths != null && ageInMonths >= 12 && ageInMonths % 12 == 0) {
                    (ageInMonths / 12).toString() to AgeUnit.YEARS
                } else {
                    (ageInMonths?.toString() ?: "") to AgeUnit.MONTHS
                }

                _uiState.update {
                    it.copy(
                        id = cat.id,
                        name = cat.name,
                        selectedBreed = matchedBreed,
                        customBreedName = cat.customBreedName ?: "",
                        useCustomBreed = matchedBreed == null && !cat.customBreedName.isNullOrBlank(),
                        photoUri = cat.photoUri,
                        age = displayAge,
                        ageUnit = unit,
                        weight = cat.weight?.toString() ?: "",
                        notes = cat.notes ?: "",
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onBreedSelected(breed: Breed?) = _uiState.update {
        it.copy(selectedBreed = breed, useCustomBreed = false)
    }
    fun onUseCustomBreedChange(value: Boolean) = _uiState.update {
        it.copy(useCustomBreed = value, selectedBreed = if (value) null else it.selectedBreed)
    }
    fun onCustomBreedNameChange(value: String) = _uiState.update { it.copy(customBreedName = value) }
    fun onPhotoSelected(uri: String?) = _uiState.update { it.copy(photoUri = uri) }
    fun onAgeChange(value: String) = _uiState.update { it.copy(age = value) }
    fun onWeightChange(value: String) = _uiState.update { it.copy(weight = value) }
    fun onNotesChange(value: String) = _uiState.update { it.copy(notes = value) }

    fun onAgeUnitChange(unit: AgeUnit) = _uiState.update { it.copy(ageUnit = unit) }

    fun save() {
        val state = _uiState.value
        if (state.name.isBlank()) return

        val ageValue = state.age.toIntOrNull()
        val ageInMonths = when {
            ageValue == null -> null
            state.ageUnit == AgeUnit.YEARS -> ageValue * 12
            else -> ageValue
        }

        val cat = MyCat(
            id = state.id,
            name = state.name.trim(),
            breedId = if (!state.useCustomBreed) state.selectedBreed?.id else null,
            customBreedName = if (state.useCustomBreed) state.customBreedName.trim().ifBlank { null } else null,
            photoUri = state.photoUri,
            age = ageInMonths,
            weight = state.weight.toFloatOrNull(),
            notes = state.notes.trim().ifBlank { null }
        )

        viewModelScope.launch {
            repository.saveMyCat(cat)
            _uiState.update { it.copy(isSaved = true) }
        }
    }

    fun delete() {
        val id = _uiState.value.id
        if (id == 0L) return
        viewModelScope.launch {
            repository.deleteMyCat(id)
            _uiState.update { it.copy(isSaved = true) }
        }
    }
}