package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place

/**
 * Each image associated with a place has a path to the file and a description we can show on screen
 */
data class ImageDetails(
    val path: String,
    val description: String
)
