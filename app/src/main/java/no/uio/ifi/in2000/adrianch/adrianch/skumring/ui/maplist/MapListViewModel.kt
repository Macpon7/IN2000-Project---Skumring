package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo

enum class MapListToggleState (val stateAsBool: Boolean) {
    MAP(false),
    LIST(true)
}

data class MapListUiState(
    val pins: List<PinInfo> = emptyList(),
    var mapListToggle: MapListToggleState = MapListToggleState.MAP
)

class MapListViewModel: ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val _mapListUiState = MutableStateFlow(MapListUiState())
    val mapListUiState: StateFlow<MapListUiState> = _mapListUiState.asStateFlow()

    init {
        loadMap()
    }

    private fun loadMap(){
        viewModelScope.launch(Dispatchers.IO){
            _mapListUiState.update { currentMapUiState ->
                val mapInfoObject = mapRepository.getPins()
                currentMapUiState.copy(pins = mapInfoObject)
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

