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
    val long: String,
    var point: Point = Point.fromLngLat(0.0, 0.0)
    //val color: String
)
