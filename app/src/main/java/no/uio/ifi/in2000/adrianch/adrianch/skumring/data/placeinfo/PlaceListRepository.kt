package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo


import android.util.Log
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapPinsDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary


private const val logTag = "PlaceListRepository"

interface PlaceListRepository {
    /**
     * Returns the full list of preset places for displaying in a list format. Combines data from two
     * sources, [PlaceDetailsDataSource] and [MapPinsDataSource].
     */
    suspend fun getPresetPlaceList(): List<PlaceSummary>
}

/**
 * Implementation of [PlaceListRepository], used for getting a list of places to display.
 *
 * @property placeDetailsDataSource an instance of [PlaceDetailsDataSource]. If none is given, a new instance is created.
 * @property mapPinsDataSource an instance of [MapPinsDataSource]. If none is given, a new instance is created.
 */
class PlaceListRepositoryImpl(
    private val placeDetailsDataSource: PlaceDetailsDataSource = PlaceDetailsDataSource(),
    private val mapPinsDataSource: MapPinsDataSource = MapPinsDataSource()
): PlaceListRepository {

    override suspend fun getPresetPlaceList(): List<PlaceSummary> {
        try {
            val listOfPins = mapPinsDataSource.fetchMapPins()
            val outList = mutableListOf<PlaceSummary>()

            //For each pin, get the corresponding details, and add a new PlaceSummary to our output list
            listOfPins.forEach {
                val details = placeDetailsDataSource.fetchPlaceDetails(it.id)
                outList.add(combinePinAndDetails(it, details))
            }

            return outList.toList()
        } catch (e: Exception) {
            Log.e(logTag, "Error fetching map pins: ${e.message}", e)
            throw e
        }
    }

    /**
     * Combines information from a [PinInfo] and [PlaceDetails] object
     * @property pinInfo the coordinates and id of a place
     * @property placeDetails the id, name and description of a place
     * @return [PlaceSummary] a combination object, containing the id, coordinates, name and description of a place
     * @throws IllegalArgumentException if the id's of these two arguments do not match
     */
    private fun combinePinAndDetails(
        pinInfo: PinInfo,
        placeDetails: PlaceDetails
    ): PlaceSummary {
        if (pinInfo.id != placeDetails.id) {
            throw IllegalArgumentException("The id's of pinInfo and placeDetails are different")
        }

        return PlaceSummary(
            id = pinInfo.id,
            lat = pinInfo.lat,
            long = pinInfo.long,
            name = placeDetails.name,
            description = placeDetails.description
        )
    }
}