package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins

import com.mapbox.geojson.Point
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo

class MapPinsDataSource {

    // Constant for logging errors:
    private val logTag : String = "MapPinsDataSource"

    suspend fun fetchMapPins(): List<PinInfo> {
        return presetPinsInfo.map {
            // Add a correct instance of Point using the string values for lat and long
            it.point = Point.fromLngLat(it.long.toDouble(), it.lat.toDouble())
            it
        }
    }
}