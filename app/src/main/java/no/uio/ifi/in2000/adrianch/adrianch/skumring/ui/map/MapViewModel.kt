package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo

data class MapUiState(
    val pins: List<PinInfo> = emptyList()
)

class MapViewModel: ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState: StateFlow<MapUiState> = _mapUiState.asStateFlow()

    init {
        loadMap()
    }

    private fun loadMap(){
        viewModelScope.launch(Dispatchers.IO){
            _mapUiState.update { currentMapUiState ->
                val mapInfoObject = mapRepository.getPins()
                currentMapUiState.copy(pins = mapInfoObject)
            }
        }
    }
}

