package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

/**
 * Contains all the information we might want to show on screen about a specific place.
 */
data class PlaceInfo (
    val id: Int,
    val name: String,
    val description: String,
    val lat: String,
    val long: String,
    val sunEvents: List<DailyEvents>
)