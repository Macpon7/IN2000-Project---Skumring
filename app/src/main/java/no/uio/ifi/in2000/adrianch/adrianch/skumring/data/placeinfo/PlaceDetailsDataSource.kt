package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceDetails

class PlaceDetailsDataSource {
    //TODO: Implement a real database, not just read from preset list

    suspend fun fetchPlaceDetails(id: Int): PlaceDetails {
        return presetPlaces.find { it.id == id } ?: PlaceDetails(
            id = 0,
            name = "",
            description = ""
        )
    }
}

val presetPlaces: List<PlaceDetails> = listOf(
    PlaceDetails(
        id = 1,
        name = "Holmenkollen",
        description = "Lorem Ipsum Dolor Sit Amet"
    ),
    PlaceDetails(
        id = 2,
        name = "Hellerudtoppen",
        description = "Lorem Ipsum Dolor Sit Amet"
    ),
    PlaceDetails(
        id = 3,
        name = "Huk",
        description = "Lorem Ipsum Dolor Sit Amet"
    )
)