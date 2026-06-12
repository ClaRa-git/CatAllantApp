package com.cfa.cda.catapp.ui.mycats

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cfa.cda.catapp.data.db.MyCat
import com.cfa.cda.catapp.data.repository.CatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyCatsUiState(
    val cats: List<MyCat> = emptyList(),
    val isLoading: Boolean = false
)

class MyCatsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = CatRepository(application)

    private val _uiState = MutableStateFlow(MyCatsUiState())
    val uiState: StateFlow<MyCatsUiState> = _uiState

    fun loadCats() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val cats = repository.getAllMyCats()
            _uiState.update { it.copy(cats = cats, isLoading = false) }
        }
    }

    fun deleteCat(id: Long) {
        viewModelScope.launch {
            repository.deleteMyCat(id)
            loadCats()
        }
    }
}