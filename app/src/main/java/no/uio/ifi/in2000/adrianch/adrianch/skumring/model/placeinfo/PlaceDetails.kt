package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

/**
 * Contains the name and description for a specific place. A place in our app context is a spot to take
 * photos from.
 *
 * @property id Unique integer id for this place
 * @property name The name of this place that we display
 * @property description A longer string with a description of this place
 */
data class PlaceDetails(
    val id: Int,
    val name: String,
    val description: String
)
