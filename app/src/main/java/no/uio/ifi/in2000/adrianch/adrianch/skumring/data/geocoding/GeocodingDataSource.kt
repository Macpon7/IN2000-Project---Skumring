package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ForwardGeocoding
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.GeocodeLocation
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ReverseGeocoding

private const val logTag: String = "GeoCodingDataSource"

private const val FORWARD_PATH = "https://api.mapbox.com/search/geocode/v6/forward?"

class GeocodingDataSource {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    /**
     * Main function that reverse geocodes and provides the name of a place given cooridnates.
     * It does however hav a weakness in how Mapbox structures the response, in that
     * it returns a sorted array of "features" they deem to be in declining order of interest.
     * As such, we assume we can fetch the necessary data (which we in vast majority of cases are)
     * from the first element of the list. If not, the place is of such low interest that
     * it will be unnamed.
     */
    suspend fun fetchReverseGeocodeLocation(lat: String, long: String): GeocodeLocation {
        val apiResponse: ReverseGeocoding = fetchReverseGeocodeResponse(lat = lat, long = long)
        return convertReverseGeocodingResponse(apiResponse)
    }
    /*
    Split up and made public for testing purposes
     */
    fun convertReverseGeocodingResponse(response: ReverseGeocoding): GeocodeLocation {
        val placeName: String = try {
            response.features[0].properties.context.place.name
        } catch (e: Exception) {
            Log.e(logTag, "Error: Placename not found. Response is empty")
            // Returns empty string if user is in area without a place name
            throw Exception("Response from reverse geocoding is empty", e)
        }

        val lat = response.features[0].geometry.coordinates[1].toString()
        val long = response.features[0].geometry.coordinates[0].toString()

        Log.d(logTag, "Got place: $placeName from coords: $lat, $long")
        return GeocodeLocation(lat = lat, long = long, placeName = placeName)
    }
    /*
    Private function that fetches the reverse geocode body
     */
    private suspend fun fetchReverseGeocodeResponse(lat: String, long: String): ReverseGeocoding {
        try {
            val path = "https://api.mapbox.com/search/geocode/v6/reverse?" +
                    "longitude=$long,&" +
                    "latitude=$lat&" +
                    "access_token=sk.eyJ1IjoidmlsamFyZGgiLCJhIjoiY2x0dTZmMjZ3MWF6NzJpcGNtajBqaWUxMSJ9.A7ntsS5LTvXYD5hnjYjEXQ"
            val response: HttpResponse = client.get(path)
            return response.body()
        } catch (e: Exception) {
            Log.e(logTag, "Error fetching place name", e)
            throw e
        }
    }

    /**
     *
     */
    suspend fun fetchForwardGeocodeLocation(address: String): List<GeocodeLocation> {
        val apiResponse: ForwardGeocoding = fetchForwardGeocodeResponse(address = address)
        val outList = mutableListOf<GeocodeLocation>()
        apiResponse.features.forEach {
            outList.add(
                GeocodeLocation(
                    lat = it.properties.coordinates.latitude.toString(),
                    long = it.properties.coordinates.longitude.toString(),
                    placeName = it.properties.full_address
                )
            )
        }

        return outList.toList()
    }

    /**
     *
     */
    private suspend fun fetchForwardGeocodeResponse(address: String): ForwardGeocoding {
        try {
            val path = FORWARD_PATH +
                    "q=$address&" +
                    "permanent=true&" +
                    "types=address&" +
                    "access_token=sk.eyJ1IjoidmlsamFyZGgiLCJhIjoiY2x0dTZmMjZ3MWF6NzJpcGNtajBqaWUxMSJ9.A7ntsS5LTvXYD5hnjYjEXQ"
            val response: HttpResponse = client.get(path)
            return response.body()
        } catch (e: Exception) {
            Log.e(logTag, "Error fetching place name", e)
            throw e
        }
    }
}