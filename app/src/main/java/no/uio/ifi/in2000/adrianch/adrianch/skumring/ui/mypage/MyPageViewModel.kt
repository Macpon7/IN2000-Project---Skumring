package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary

data class MyPageUiState(
    val pins: List<PinInfo> = emptyList(),
    val places: List<PlaceSummary> = emptyList(),

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "No error",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

private const val logTag = "MyPageViewModel"

class MyPageViewModel : ViewModel() {

    // TODO: Make own repository for places that the user saves, use this as a placeholder meanwhile
    private val placeListRepository = PlaceListRepositoryImpl()


    private val _mapListUiState = MutableStateFlow(MyPageUiState())
    val myListUiState: StateFlow<MyPageUiState> = _mapListUiState

    init {
        loadList()
    }

    /**
     * Load the list of places
     */
    private fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            _mapListUiState.update { currentMapListUiState ->
                try {
                    // TODO: This will be changed with the repository of the places the user saves
                    val placeSummaryList = placeListRepository.getPresetPlaceList()
                    currentMapListUiState.copy(places = placeSummaryList)
                } catch(e: Exception) {
                    Log.e(logTag, "Error getting pins in loadList", e)
                    currentMapListUiState.copy(showSnackbar = true,
                        errorMessage = "Error getting pins in loadList")
                }
            }
        }
    }

    /**
     * showSnackbar is set to false and the snackbar disappear
     */
    fun snackbarDismissed() {
        _mapListUiState.update { currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
    }

    /**
     *    For refreshing when you use snackbar in MyPageScreen:
     */
    fun refresh() {
        _mapListUiState.update {currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false) }
        viewModelScope.launch (Dispatchers.IO) {
            loadList()
        }
    }
}