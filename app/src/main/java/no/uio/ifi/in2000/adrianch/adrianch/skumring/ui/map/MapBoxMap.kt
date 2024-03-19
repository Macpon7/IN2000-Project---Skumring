package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.map

import android.graphics.Bitmap
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
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

// Testing purposes only, please ignore
private class TestPin(lat: Double, long: Double, val name: String) {
    val point: Point = Point.fromLngLat(long, lat)
}
private val varden = TestPin(58.9516263, 5.7264998, "Vårlivarden")
private val kollen = TestPin(59.9640303, 10.6651817, "Holmenkollen")
private val hellerud = TestPin(59.9121057,10.8547637, "Hellerudtoppen")
private val huk = TestPin(59.8953493,10.6754127, "Huk Strand")

private val testList = listOf(varden, kollen, hellerud, huk)

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
        context.getDrawable(R.drawable.location_on)!!.toBitmap()
    }
    // Manages point annotations on the mapscreen - Descriptions of the pins
    var pointAnnotationManager: PointAnnotationManager? by remember {
        mutableStateOf(null)
    }

    // AndroidView is a wrapper that takes the object we want to initialize
    // as a factory argument.
    // "factory" launches only during first composition, update keeps track of changes.
    // TODO Keep it composed lol
    @OptIn(ExperimentalComposeUiApi::class)
    AndroidView(
        factory = {
            MapView(it).also { mapView ->
                mapView.mapboxMap.loadStyle(Style.OUTDOORS)
                // Fetching and managing the annotations
                val annotationApi = mapView.annotations
                pointAnnotationManager = annotationApi.createPointAnnotationManager()
                pointAnnotationManager?.let { pam ->
                    // TODO Have it fetch from local data with favourites
                    testList.forEach { pin ->
                        makeAnnotation(pam, marker, pin.point, pin.name)
                    }
                }
            }
        },
        // Updates when recomposed
        update = { mapView ->
            // If a point is provided, it updates the manager by deleting
            // existing annotations and making a new one with the provided point
            if (point != null) {
                pointAnnotationManager?.let {
                    //it.deleteAll()

                    makeAnnotation(it, marker, point, "Ole-Johan Dahls Hus")

                    mapView.mapboxMap
                        // Built in function has the camera fly nicely to the new point
                        .flyTo(CameraOptions.Builder().zoom(10.0).center(point).build())
                }
            }
            // Tells AndroidView that it doesn't need additional updates
            NoOpUpdate
        },
        modifier = modifier
    )
}

// Function for making more pins on the map
// known to mapbox as annotations
private fun makeAnnotation(
    pointAnnotationManager: PointAnnotationManager,
    marker: Bitmap,
    point: Point,
    description: String) {
    val pointAnnotationOptions = PointAnnotationOptions()
        .withPoint(point)
        .withIconImage(marker)
        .withTextField(description)
        .withIconOffset(listOf(0.0, 20.0))

    pointAnnotationManager.create(pointAnnotationOptions)
}