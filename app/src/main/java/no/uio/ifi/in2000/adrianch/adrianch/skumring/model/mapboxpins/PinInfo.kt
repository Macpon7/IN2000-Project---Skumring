package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins

import com.mapbox.geojson.Point

/**
 * Contains and id and coordinated for a pin on the map
 * @property id Unique integer ID for each place
 * @property lat Latitude coordinate of this place
 * @property long Longitude coordinate of this place
 */
data class PinInfo(
    val id: Int,
    val lat: String,
    val long: String
    //val color: String
) {
    val point: Point = Point.fromLngLat(long.toDouble(), lat.toDouble())
}
