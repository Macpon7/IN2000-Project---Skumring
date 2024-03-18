package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.map

import androidx.lifecycle.ViewModel
import com.mapbox.maps.MapView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class MapBoxUiState(
    //val title: String = "MapBox"
    val mapView : MapView? = null
)

private const val logTag = "MapBox"

/**
 * ViewModel for MapBox
 */
class MapBoxViewModel() : ViewModel() {
    private val _mapBoxUiState = MutableStateFlow(MapBoxUiState())
    val mapBoxUiState: StateFlow<MapBoxUiState> = _mapBoxUiState.asStateFlow()

}