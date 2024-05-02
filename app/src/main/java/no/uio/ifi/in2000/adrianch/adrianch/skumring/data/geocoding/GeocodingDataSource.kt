package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ReverseGeocoding
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ReverseGeocodeLocation

private const val logTag: String = "GeoCodingDataSource"

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
    suspend fun fetchReverseGeocodeLocation(lat: String, long: String): ReverseGeocodeLocation {
        val apiResponse: ReverseGeocoding = fetchReverseGeocodeResponse(lat = lat, long = long)
        val placeName: String = try {
            apiResponse.features[0].properties.context.place.name
        } catch (e: Exception) {
            Log.d(logTag, "Error: Placename not found", e)
            // Returns empty string if user is in area without a place name
            ""
        }
        return ReverseGeocodeLocation(lat = lat, long = long, placeName = placeName)
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
}