package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.DefaultMonotonicFrameClock
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary


data class FavoritesUiState @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class) constructor(
    val places: List<PlaceSummary> = emptyList(),
    var clickedId: Int = 1,


    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "No error",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

private const val logTag = "FavoritesViewModel"

class FavoritesViewModel(placeInfoRepository: PlaceInfoRepository): ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val placeListRepository = PlaceListRepositoryImpl()

    private val _favoritesUiState = MutableStateFlow(FavoritesUiState())
    val favoritesUiState: StateFlow<FavoritesUiState> =_favoritesUiState.asStateFlow()

    init {
           loadList()
    }


    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    private fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            _favoritesUiState.update { currentfavoritesUiState ->
                try {
                    val placeSummaryList = placeListRepository.getPresetPlaceList()
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

}


