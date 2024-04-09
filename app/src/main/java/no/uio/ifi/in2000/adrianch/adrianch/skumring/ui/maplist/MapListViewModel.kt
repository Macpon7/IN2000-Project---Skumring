package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary

enum class MapListToggleState (val stateAsBool: Boolean) {
    MAP(false),
    LIST(true)
}

data class MapListUiState(
    val pins: List<PinInfo> = emptyList(),
    val places: List<PlaceSummary> = emptyList(),
    var mapListToggle: MapListToggleState = MapListToggleState.MAP
)

private const val logTag = "MapListViewModel"

class MapListViewModel: ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val placeListRepository = PlaceListRepositoryImpl()
    private val _mapListUiState = MutableStateFlow(MapListUiState())
    val mapListUiState: StateFlow<MapListUiState> = _mapListUiState.asStateFlow()

    init {
        loadMap()
        loadList()
    }

    private fun loadMap(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                _mapListUiState.update { currentMapUiState ->
                    val mapInfoObject = mapRepository.getPins()
                    currentMapUiState.copy(pins = mapInfoObject)
                }
            } catch(e: Exception) {
                Log.e(logTag, "Error getting pins, failed updating state", e)
            }
        }
    }

    private fun loadList(){
        viewModelScope.launch(Dispatchers.IO) {
            try { // Probably unnecessary but here we are
                _mapListUiState.update { currentMapListUiState ->
                    val placeSummaryList = placeListRepository.getPresetPlaceList()
                    currentMapListUiState.copy(places = placeSummaryList)
                }
            } catch(e: Exception) {
                Log.e(logTag, "Error getting pins", e)
            }
        }
    }

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
}

