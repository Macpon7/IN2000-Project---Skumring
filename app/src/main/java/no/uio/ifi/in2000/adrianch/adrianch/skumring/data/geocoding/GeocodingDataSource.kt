package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.Feature
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.Place
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.Properties
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ReverseGeocoding
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ReverseGeocodeLocation

private const val logTag: String = "GeoCodingDataSource"

class GeocodingDataSource {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }
    suspend fun fetchReverseGeocodeLocation(lat: String, long: String): ReverseGeocodeLocation {
        val apiResponse: ReverseGeocoding = fetchReverseGeocode(lat = lat, long = long)
        val placeName: String = try {
            apiResponse.features[0].properties.context.place.name
        } catch (e: Exception) {
            Log.d(logTag, "Error: Placename not found", e)
            "Placename not found"
        }
        return ReverseGeocodeLocation(lat = lat, long = long, placeName = placeName)
    }
    /*
    Private function that fetches the reverse geocode body
     */
    private suspend fun fetchReverseGeocode(lat: String, long: String): ReverseGeocoding {
        try {
            val path = "https://api.mapbox.com/search/geocode/v6/reverse?longitude=$long,&latitude=$lat&access_token=sk.eyJ1IjoidmlsamFyZGgiLCJhIjoiY2x0dTZmMjZ3MWF6NzJpcGNtajBqaWUxMSJ9.A7ntsS5LTvXYD5hnjYjEXQ"
            val response: HttpResponse = client.get(path)
            return response.body()
        } catch (e: Exception) {
            Log.e(logTag, "Error fetching place name", e)
            throw e
        }
    }
}