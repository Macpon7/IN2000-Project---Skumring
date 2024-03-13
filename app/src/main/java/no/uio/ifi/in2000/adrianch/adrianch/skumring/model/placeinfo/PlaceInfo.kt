package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

/**
 * PlaceInfo all the information we might want to show on screen about a specific place.
 */
data class PlaceInfo (
    val name: String,
    val description: String,
    val latitude: String,
    val longitude: String,
    val sunEvents: List<DailyEvents>
)