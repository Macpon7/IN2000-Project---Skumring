package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

/**
 * Data class used for retrieving information about each specific place
 *
 * Contains the name and description for a specific place. A place in our app context is a spot to take
 * photos from.
 */
data class PlaceDetails(
    val name: String,
    val description: String
)
