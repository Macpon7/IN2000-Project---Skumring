package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import android.accounts.NetworkErrorException
import android.util.Log
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapPinsDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary
import org.json.JSONException
import java.io.IOException
import java.lang.IllegalArgumentException
import java.time.DateTimeException

private const val logTag = "PlaceListRepository"

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
        try {
            val listOfPins = mapPinsDataSource.fetchMapPins()
            val outList = mutableListOf<PlaceSummary>()

            //For each pin, get the corresponding details, and add a new PlaceSummary to our output list
            listOfPins.forEach {
                try {
                    val details = placeDetailsDataSource.fetchPlaceDetails(it.id)
                    outList.add(
                        PlaceSummary(
                            id = it.id,
                            lat = it.lat,
                            long = it.long,
                            name = details.name,
                            description = details.description
                        )
                    )

                } catch (e: Exception) {
                    //Handle exception occured during fetching place details for a pin
                    Log.e(logTag, "Error fetching place for pin with ID ${it.id}: ${e.message}", e)
                    throw e
                }
            }
            return outList.toList()

        } catch (e: IOException) {
            //Handle IOException, generally for I/O operations
            Log.e(logTag, "IOException occured in getPlaceList: ${e.message}", e)
            throw e
        } catch (e: JSONException) {
            //Handle JSONException
            Log.e(logTag, "JSONException occured in getPlaceList: ${e.message}", e)
            throw e
        } catch (e: NullPointerException) {
            //Handle NullPointerException
            Log.e(logTag,"NullPointerException occured in getPlaceList: ${e.message}", e)
            throw e
        } catch (e: IllegalArgumentException) {
            // Handle IllegalArgumentException
            Log.e(logTag, "IllegalArgumentException occured in getPlaceList: ${e.message}", e)
            throw e
        } catch (e: NetworkErrorException) {
            //Handle NetworkErrorException, specifically for networkerrors
            Log.e(logTag, "NetworkErrorException occured in getPlaceList: ${e.message}", e)
            throw e
            //Handle exception occured during fetching map pins
        } catch (e: DateTimeException) {
            //Handle DateTimeException
            Log.e(logTag, "DateTimeException occured in getPlaceList: ${e.message}", e)
            throw e
        } catch (e: Exception) {
            Log.e(logTag, "Error fetching map pins: ${e.message}", e)
            throw e
        }
    }
}