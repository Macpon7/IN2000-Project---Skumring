package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.map

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
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

    fun makeAnnotation(
        pointAnnotationManager: PointAnnotationManager,
        marker: Bitmap,
        point: Point
    ) {
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(marker)
        //.withTextField(description)
        //.withIconOffset(listOf(0.0, 20.0))

        pointAnnotationManager.create(pointAnnotationOptions)
    }
}
