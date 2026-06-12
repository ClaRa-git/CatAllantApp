package com.cfa.cda.catapp.ui.breeds

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cfa.cda.catapp.data.db.FavoriteEntry
import com.cfa.cda.catapp.data.model.Breed
import com.cfa.cda.catapp.data.repository.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SortOption(val label: String) {
    NAME_ASC("Nom (A-Z)"),
    RATING_DESC("Note"),
    FAVORITES_FIRST("Favoris d'abord")
}

data class BreedsUiState(
    val allBreeds: List<Breed> = emptyList(),
    val favorites: Map<String, FavoriteEntry> = emptyMap(),
    val searchQuery: String = "",
    val sortOption: SortOption = SortOption.NAME_ASC,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val filteredBreeds: List<Breed>
        get() {
            val filtered = if (searchQuery.isBlank()) {
                allBreeds
            } else {
                allBreeds.filter { it.name.contains(searchQuery, ignoreCase = true) }
            }
            return when (sortOption) {
                SortOption.NAME_ASC -> filtered.sortedBy { it.name }
                SortOption.RATING_DESC -> filtered.sortedByDescending { favorites[it.id]?.rating ?: 0f }
                SortOption.FAVORITES_FIRST -> filtered.sortedByDescending {
                    (favorites[it.id]?.isFavorite == true)
                }
            }
        }
}

class BreedsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CatRepository(application)

    private val _uiState = MutableStateFlow(BreedsUiState())
    val uiState: StateFlow<BreedsUiState> = _uiState

    init {
        loadBreeds()
    }

    fun loadBreeds() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val breeds = repository.getAllBreeds().sortedBy { it.name }
                val favoritesMap = breeds.mapNotNull { breed ->
                    repository.getFavoriteEntry(breed.id)?.let { breed.id to it }
                }.toMap()
                _uiState.update {
                    it.copy(allBreeds = breeds, favorites = favoritesMap, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Erreur de chargement") }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onSortOptionChange(option: SortOption) {
        _uiState.update { it.copy(sortOption = option) }
    }
}