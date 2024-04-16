package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo

private const val logTag = "PlaceInfoViewModel"

data class PlaceInfoUiState(
    var placeInfo: PlaceInfo = PlaceInfo(
        id = 0,
        name = "",
        description = "",
        lat = "",
        long = "",
        isFavourite = false,
        isCustomPlace = false,
        hasNotification = false,
        images = emptyList(),
        sunEvents = emptyList()
        ),

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable for if we should show loading wheel or not:
    var isLoading: Boolean = true,
    // Variable that change according to the error message we get:
    var errorMessage: String = "No error",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)


class PlaceInfoViewModel(private val placeInfoRepository: PlaceInfoRepository): ViewModel() {
    private val oldPlaceInfoRepository: OldPlaceInfoRepository = OldPlaceInfoRepositoryImpl()
    private val _placeInfoUiState = MutableStateFlow(PlaceInfoUiState())

    val placeInfoUiState: StateFlow<PlaceInfoUiState> = _placeInfoUiState.asStateFlow()

    fun loadPlaceInfo(id: Int){
        val job = viewModelScope.launch(Dispatchers.IO){
            Log.d(logTag, "loadPlaceInfo called")
            _placeInfoUiState.update { currentPlaceInfoUiState ->
                try {
                    val placeInfoObject = placeInfoRepository.getPlace(id)
                    currentPlaceInfoUiState.copy(placeInfo = placeInfoObject, isLoading = false)
                } catch(e: Exception) {
                    Log.e(logTag, "Error getting PlaceInfo object for place with id: $id", e)
                    currentPlaceInfoUiState.copy(showSnackbar = true,
                        errorMessage = "Error getting pins in loadPlaceInfo")
                }
            }
        }
        job.invokeOnCompletion {
            Log.d(logTag, "New place in UiState: ${placeInfoUiState.value.placeInfo}")
        }
    }

    /**
     * Set showSnackbar to false, so when the snackbar refresh it will be shown again
      */
    fun snackbarDismissed() {
        _placeInfoUiState.update { currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
    }

    /**
     *  This function refresh loadPlaceInfo when you use snackbar in MapListScreen:
     */
    fun refresh(id: Int = 0 ) {
        _placeInfoUiState.update {currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch (Dispatchers.IO) {
            loadPlaceInfo(id)
        }
    }
}





