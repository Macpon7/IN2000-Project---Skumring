package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.DefaultMonotonicFrameClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.TestRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary

enum class MapListToggleState (val stateAsBool: Boolean) {
    MAP(false),
    LIST(true)
}

data class MapListUiState @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class) constructor(
    val pins: List<PinInfo> = emptyList(),
    val places: List<PlaceSummary> = emptyList(),
    var clickedId: Int = 1,
    var mapListToggle: MapListToggleState = MapListToggleState.MAP,
    var sheetState: SheetState = SheetState(skipPartiallyExpanded = true),
    var showBottomSheet: Boolean = false
)

private const val logTag = "MapListViewModel"

class MapListViewModel(testRepo: TestRepository): ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val placeListRepository = PlaceListRepositoryImpl()

    private val _mapListUiState = MutableStateFlow(MapListUiState())
    val mapListUiState: StateFlow<MapListUiState> = _mapListUiState.asStateFlow()

    init {
        loadMap()
        loadList()
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
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

    @OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
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
}

