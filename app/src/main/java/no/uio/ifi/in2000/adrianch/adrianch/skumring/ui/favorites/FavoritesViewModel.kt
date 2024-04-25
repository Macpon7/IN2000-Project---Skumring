package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ApplicationSkumring
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo


data class FavoritesUiState @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class) constructor(
    val places: List<PlaceInfo> = emptyList(),

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "No error",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

private const val logTag = "FavoritesViewModel"

class FavoritesViewModel(private val placeRepository: PlaceRepository): ViewModel() {
    private val _favoritesUiState = MutableStateFlow(FavoritesUiState())
    val favoritesUiState: StateFlow<FavoritesUiState> = _favoritesUiState.asStateFlow()

    init {
           //loadList()
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            _favoritesUiState.update { currentfavoritesUiState ->
                try {
                    val placeSummaryList = placeRepository.getFavourites()
                    currentfavoritesUiState.copy(places = placeSummaryList)
                } catch(e: Exception) {
                    Log.e(logTag, "Error getting pins in loadList", e)
                    currentfavoritesUiState.copy(showSnackbar = true,
                        errorMessage = "Error getting pins in loadList")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun toggleFavourite(place: PlaceInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (place.isFavourite) {
                placeRepository.unmakeFavourite(id = place.id)
                _favoritesUiState.update { currentFavoritesUiState ->
                    currentFavoritesUiState.copy(
                        places = placeRepository.getFavourites()
                    )
                }
            } else {
                placeRepository.makeFavourite(id = place.id)
                _favoritesUiState.update { currentFavoritesUiState ->
                    currentFavoritesUiState.copy(
                        places = placeRepository.getFavourites()
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun snackbarDismissed() {
        _favoritesUiState.update { currentfavoritesUiState->
            currentfavoritesUiState.copy(showSnackbar = false)
        }
    }

    // For refreshing when you use snackbar in MapListScreen:
    @OptIn(ExperimentalMaterial3Api::class)
    fun refreshList() {
        _favoritesUiState.update {currentfavoritesUiState ->
            currentfavoritesUiState.copy(showSnackbar = false) }
        viewModelScope.launch (Dispatchers.IO) {
            loadList()
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return FavoritesViewModel(
                    placeRepository = (application as ApplicationSkumring).dbRepository
                ) as T
            }
        }
    }
}


