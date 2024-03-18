package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.map

import no.uio.ifi.in2000.adrianch.adrianch.skumring.R

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.NoOpUpdate
import androidx.core.graphics.drawable.toBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager

/**
 * MapBoxMap composable - A mapbox with saveable state we can squish and
 * squeeze into the boxes and screens as we see fit.
 * Takes modifier as such and a point we want to start it on
 */
@Composable
fun MapBoxMap(
    modifier: Modifier = Modifier,
    point: Point?,
) {
    // Keep track of context, markers and points to improve performance
    val context = LocalContext.current
    val marker = remember(context) {
        // We want to insert a custom marker here lol
        context.getDrawable(R.drawable.solnedgang)!!.toBitmap()
    }
    // Manages point annotations on the mapscreen - Descriptions of the pins
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    // AndroidView is a wrapper that takes the object we want to initialize
    // as a factory argument.
    // "factory" launches only during first composition, update keeps track
    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.mapboxMap.loadStyleUri(Style.LIGHT)
                // Fetching and managing the annotations
                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
            }
        },
        // Updates when recomposed
        update = { mapView ->
            // If a point is provided, it updates the manager by deleting
            // existing annotations and making a new one with the provided point
            if (point != null) {
                pointAnnotationManager?.let {
                    it.deleteAll()
                    val pointAnnotationOptions = PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage(marker)

                    it.create(pointAnnotationOptions)
                    mapView.mapboxMap
                        // Built in function has the camera fly nicely to the new point
                        .flyTo(CameraOptions.Builder().zoom(15.0).center(point).build())
                }
            }
            NoOpUpdate
        },
        modifier = modifier
    )
}