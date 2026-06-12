package com.cfa.cda.catapp.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cfa.cda.catapp.data.model.Breed
import com.cfa.cda.catapp.data.repository.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BreedDetailUiState(
    val breed: Breed? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false,
    val rating: Float = 0f
)

class BreedDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CatRepository(application)

    private val _uiState = MutableStateFlow(BreedDetailUiState())
    val uiState: StateFlow<BreedDetailUiState> = _uiState

    fun loadBreed(breedId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val breed = repository.getBreedById(breedId)
                val favEntry = repository.getFavoriteEntry(breedId)
                _uiState.update {
                    it.copy(
                        breed = breed,
                        isLoading = false,
                        isFavorite = favEntry?.isFavorite ?: false,
                        rating = favEntry?.rating ?: 0f
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erreur de chargement") }
            }
        }
    }

    fun toggleFavorite() {
        val breed = _uiState.value.breed ?: return
        val currentlyFavorite = _uiState.value.isFavorite
        repository.toggleFavorite(breed, currentlyFavorite)
        _uiState.update { it.copy(isFavorite = !currentlyFavorite) }
    }

    fun setRating(rating: Float) {
        val breed = _uiState.value.breed ?: return
        repository.setRating(breed, rating)
        _uiState.update { it.copy(rating = rating) }
    }
}