package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapPinsDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary

interface PlaceListRepository {
    suspend fun getPlaceList(): List<PlaceSummary>
}

class PlaceListRepositoryImpl(
    private val placeDetailsDataSource: PlaceDetailsDataSource = PlaceDetailsDataSource(),
    private val mapPinsDataSource: MapPinsDataSource = MapPinsDataSource()
): PlaceListRepository {
    /**
     * Combines the two different sources of info for pins and place details, so that our MapListScreen
     * can display a list of places with name and description, while also being able to send the coordinates
     * and id of each place as arguments when the user clicks on a list item and goes to a PlaceInfoScreen
     */
    override suspend fun getPlaceList(): List<PlaceSummary> {
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