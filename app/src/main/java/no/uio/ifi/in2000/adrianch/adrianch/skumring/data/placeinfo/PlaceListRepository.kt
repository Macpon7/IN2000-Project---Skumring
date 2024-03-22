package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapPinsDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary

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
        val listOfPins = mapPinsDataSource.fetchMapPins()
        val outList = mutableListOf<PlaceSummary>()

        //For each pin, get the corresponding details, and add a new PlaceSummary to our output list
        listOfPins.forEach {
            val details = placeDetailsDataSource.fetchPlaceDetails(it.id)
            outList.add(PlaceSummary(
                id = it.id,
                lat = it.lat,
                long = it.long,
                name = details.name,
                description = details.description
            )
            )
        }

        return outList.toList()
    }
}