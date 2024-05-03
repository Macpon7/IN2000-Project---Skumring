package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo

enum class MapListToggleState (val stateAsBool: Boolean) {
    MAP(stateAsBool = false),
    LIST(stateAsBool = true)
}

data class MapListUiState @OptIn(ExperimentalMaterial3Api::class) constructor(
    val pins: List<PinInfo> = emptyList(),
    val places: List<PlaceInfo> = emptyList(),
    var clickedId: Int = 1,
    var mapListToggle: MapListToggleState = MapListToggleState.MAP,
    var sheetState: SheetState = SheetState(skipPartiallyExpanded = false),
    var showBottomSheet: Boolean = false,
    var userLat: String = "0",
    var userLong: String = "0",
    var userBearing: Float = 0.0f,
    var userLocUpdated: Boolean = false,

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState(),

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

    )

private const val logTag = "MapListViewModel"

@SuppressLint("StaticFieldLeak")
class MapListViewModel(
    private val context: Context,
    private val placeRepository: PlaceRepository): ViewModel() {

    private val _mapListUiState = MutableStateFlow(MapListUiState())
    val mapListUiState: StateFlow<MapListUiState> = _mapListUiState.asStateFlow()

    private val userLocationRepository: UserLocationRepository = UserLocationRepositoryImpl(context = context)

    // TODO make this work instead of having the user press a button
    init {
        //loadPlaces()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun loadPlaces(){
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(logTag, "Loading all places from DB")
            _mapListUiState.update { currentMapListUiState ->
                try {
                    // Get all places from DB and make corresponding PinInfo objects
                    val places = placeRepository.getAllPlaces()
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
                    currentMapListUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_getting_places_from_database))
                }
            }
        }.invokeOnCompletion {
            Log.d(logTag, "New places list: ${mapListUiState.value.places}")
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
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
        viewModelScope.launch(Dispatchers.IO) {
            withContext(DefaultMonotonicFrameClock) {_mapListUiState.value.sheetState.hide()}
        }.invokeOnCompletion { _mapListUiState.update { currentMapListUiState ->
            currentMapListUiState.copy(
                showBottomSheet = false
            )
        } }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun showBottomSheet(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _mapListUiState.update { currentMapListUiState ->
                currentMapListUiState.copy(
                    showBottomSheet = true,
                    clickedId = id
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun toggleFavourite(place: PlaceInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (place.isFavourite) {
                placeRepository.unmakeFavourite(id = place.id)
                _mapListUiState.update { currentMapListUiState ->
                    currentMapListUiState.copy(
                        places = placeRepository.getAllPlaces()
                    )
                }
            } else {
                placeRepository.makeFavourite(id = place.id)
                _mapListUiState.update { currentMapListUiState ->
                    currentMapListUiState.copy(
                        places = placeRepository.getAllPlaces()
                    )
                }
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
    fun refreshPlaces() {
        _mapListUiState.update {currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch (Dispatchers.IO) {
            loadPlaces()
            updateUserLocation()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateUserLocation() {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                _mapListUiState.update { currentMapUiState ->
                    val userLoc = userLocationRepository.getUserLocation()
                    Log.d(logTag, "Updating userlocation ${userLoc.long}, ${userLoc.lat}")
                    currentMapUiState.copy(
                        userLat = userLoc.lat,
                        userLong = userLoc.long,
                        userBearing = userLoc.bearing,
                        userLocUpdated = true
                    )
                }
            } catch (e: Exception) {
                Log.e(logTag, "Error updating user location", e)
            }
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
                    placeRepository = (application as ApplicationSkumring).dbRepository,
                    context = application.context
                ) as T
            }
        }
    }
}

