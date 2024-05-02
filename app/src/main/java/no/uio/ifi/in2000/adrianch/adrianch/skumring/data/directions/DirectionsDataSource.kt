package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.directions

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.Directions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.MeansOfTransportation
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.TravelDurationDistance

private const val logTag = "DirectionsDataSource"

class DirectionsDataSource {

    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    /**
     * Calls Mabpox' Directions API which provides a travel route between two sets of coordinates
     * and suggests time and distance given a means of transportation. The response returns meters
     * and seconds which are converted to kilometers and hours:minutes, which for now is all we're
     * interested in. This API as well returns an array of items, but they're indexed and structured
     * in a way that the first element will contain the information we're interested in, known as
     * "legs". If it can't find a route it will create [TravelDurationDistance] object with empty
     * strings to be interpreted by a string template to inform the user.
     *
     * If the route does not exist, the object will return string values "xx" to imply route couldn't
     * be calculated.
     */
    suspend fun fetchTravelDurationDistance(
        fromLat: String,
        fromLong: String,
        toLat: String,
        toLong: String,
        meansOfTransportation: MeansOfTransportation
    ): TravelDurationDistance {
        try {
            val apiResponse = fetchDirectionsResponse(
                fromLat = fromLat,
                fromLong = fromLong,
                toLat = toLat,
                toLong = toLong,
                meansOfTransportation = meansOfTransportation
            )
            val durationMinutes: String = secondsToMinutes(apiResponse.routes[0].legs[0].duration)
            val durationHours: String = secondsToHours(apiResponse.routes[0].legs[0].duration)
            val distance: String = "%.1f".format((apiResponse.routes[0].legs[0].distance/1000).toFloat())

            return TravelDurationDistance(
                meansOfTransportation = meansOfTransportation,
                distance = distance,
                durationHours = durationHours,
                durationMinutes = durationMinutes,
            )
        } catch (e: Exception) {
            Log.e(logTag,"Failed to calculate route - None available", e)
            return TravelDurationDistance(
                meansOfTransportation = meansOfTransportation,
                distance = "xx",
                durationMinutes = "xx",
                durationHours = "xx",
            )
        }
    }

    /*
    Function that returns the response body from the API call
     */
    private suspend fun fetchDirectionsResponse(
        fromLat: String,
        fromLong: String,
        toLat: String,
        toLong: String,
        meansOfTransportation: MeansOfTransportation
    ) : Directions {
        try {
            val path = "https://api.mapbox.com/directions/v5/mapbox/" +
                    "${meansOfTransportation.apiCall}/" +
                    "$fromLong,$fromLat;" +
                    "$toLong,$toLat?" +
                    "geometries=geojson&access_token=sk.eyJ1IjoidmlsamFyZGgiLCJhIjoiY2x0dTZmMjZ3MWF6NzJpcGNtajBqaWUxMSJ9.A7ntsS5LTvXYD5hnjYjEXQ"
            val response: HttpResponse = client.get(path)
            return response.body()
        } catch (e: Exception) {
            Log.e(logTag, "Error fetching directions", e)
            throw e
        }
    }

    /*
    Small help functions since the directions api returns duration in seconds, we
    convert it to neatly formatted strings instead
     */
    private fun secondsToHours (duration: Double): String {
        val hours: Int = duration.toInt() / 3600
        return hours.toString()
    }
    private fun secondsToMinutes (duration: Double): String {
        val minutes: Int = (duration.toInt() % 3600) / 60
        return minutes.toString()
    }
}