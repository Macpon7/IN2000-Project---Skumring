package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceDetails

class PlaceDetailsDataSource {
    //TODO: Implement a real database, not just read from preset list

    suspend fun fetchPlaceDetails(id: Int): PlaceDetails {
        return presetPlacesDetails.find { it.id == id } ?: PlaceDetails(
            id = 0,
            name = "",
            description = ""
        )
    }
}