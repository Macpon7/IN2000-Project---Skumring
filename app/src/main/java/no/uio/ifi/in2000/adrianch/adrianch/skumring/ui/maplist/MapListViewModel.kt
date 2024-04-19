package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.DefaultMonotonicFrameClock
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
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ApplicationSkumring
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlacesRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo

enum class MapListToggleState (val stateAsBool: Boolean) {
    MAP(stateAsBool = false),
    LIST(stateAsBool = true)
}

data class MapListUiState @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class) constructor(
    val pins: List<PinInfo> = emptyList(),
    val places: List<PlaceInfo> = emptyList(),
    var clickedId: Int = 1,
    var mapListToggle: MapListToggleState = MapListToggleState.MAP,
    var sheetState: SheetState = SheetState(skipPartiallyExpanded = true),
    var showBottomSheet: Boolean = false,

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "No error",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

private const val logTag = "MapListViewModel"

class MapListViewModel(private val placesRepository: PlacesRepository): ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val placeListRepository = PlaceListRepositoryImpl()

    private val _mapListUiState = MutableStateFlow(MapListUiState())
    val mapListUiState: StateFlow<MapListUiState> = _mapListUiState.asStateFlow()

    // TODO make this work instead of having the user press a button
    init {
        loadPlaces()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun loadMap(){
        viewModelScope.launch(Dispatchers.IO){
            _mapListUiState.update { currentMapUiState ->
                try {
                    val mapInfoObject = mapRepository.getPins()
                    currentMapUiState.copy(pins = mapInfoObject)
                } catch(e: Exception) {
                    Log.e(logTag, "Error getting pins, failed updating state in loadMap", e)
                    currentMapUiState.copy(showSnackbar = true,
                        errorMessage = "Error getting pins, failed updating state in loadMap")
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    fun loadPlaces(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(logTag, "Loading all places from DB")
            _mapListUiState.update { currentMapListUiState ->
                try {
                    // Get all places from DB and make corresponding PinInfo objects
                    val places = placesRepository.getAllPlaces()
                    val pins = places.map {
                        PinInfo(
                            id = it.id,
                            lat = it.lat,
                            long = it.long
                        )
                    }

                    currentMapListUiState.copy(places = places, pins = pins)
                } catch(e: Exception) {
                    Log.e(logTag, "Error getting places from DB", e)
                    currentMapListUiState.copy(showSnackbar = true,
                        errorMessage = "Error getting places from database")
                }
            }
        }.invokeOnCompletion {
            Log.d(logTag, "New places list: ${mapListUiState.value.places}")
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
    fun toggleMapListState() {
        viewModelScope.launch(Dispatchers.IO) {
            _mapListUiState.update { currentMapListUiState ->
                if (currentMapListUiState.mapListToggle == MapListToggleState.MAP) {
                    currentMapListUiState.copy(mapListToggle = MapListToggleState.LIST)
                } else {
                    currentMapListUiState.copy(mapListToggle = MapListToggleState.MAP)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun hideBottomSheet() {
        viewModelScope.launch {
            withContext(DefaultMonotonicFrameClock) {_mapListUiState.value.sheetState.hide()}
        }.invokeOnCompletion { _mapListUiState.update { currentMapListUiState ->
            currentMapListUiState.copy(
                showBottomSheet = false
            )
        } }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun showBottomSheet(id: Int) {
        viewModelScope.launch {
            _mapListUiState.update { currentMapListUiState ->
                currentMapListUiState.copy(
                    showBottomSheet = true,
                    clickedId = id
                )
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    fun snackbarDismissed() {
        _mapListUiState.update { currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
    }

    // For refreshing when you use snackbar in MapListScreen:
    @OptIn(ExperimentalMaterial3Api::class)
    fun refreshList() {
        _mapListUiState.update {currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false) }
        viewModelScope.launch (Dispatchers.IO) {
            loadPlaces()
        }
    }

    // For refreshing when you use snackbar in MapListScreen:
    @OptIn(ExperimentalMaterial3Api::class)
    fun refreshPlaces() {
        _mapListUiState.update {currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch (Dispatchers.IO) {
            loadPlaces()
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

                return MapListViewModel(
                    placesRepository = (application as ApplicationSkumring).dbRepository
                ) as T
            }
        }
    }
}

