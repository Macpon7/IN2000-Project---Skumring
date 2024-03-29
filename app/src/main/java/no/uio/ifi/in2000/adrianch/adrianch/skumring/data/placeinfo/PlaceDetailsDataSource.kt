package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import android.util.Log
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceDetails

class PlaceDetailsDataSource {
    //TODO: Implement a real database, not just read from preset list

    // Constant for logging errors:
    private val logTag : String = ""

    suspend fun fetchPlaceDetails(id: Int): PlaceDetails {
        try {
            return presetPlacesDetails.find { it.id == id } ?: PlaceDetails(
                id = 0,
                name = "",
                description = ""
            )
        } catch (e : Exception) {
            Log.e(logTag, "Unknown error: ${e.message} in fetchPlaceDetails" , e)
            throw e
        }

    }
}