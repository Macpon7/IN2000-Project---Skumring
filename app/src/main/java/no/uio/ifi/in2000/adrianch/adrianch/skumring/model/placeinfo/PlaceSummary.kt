package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

/**
 * Data class containing everything we need to make a list of clickable objects that display
 * a name and description, and also redirect to the proper PlaceInfoScreen
 */
data class PlaceSummary(
    val id: Int,
    val lat: String,
    val long: String,
    val name: String,
    val description: String
)
