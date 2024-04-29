package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.directions

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
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

    suspend fun fetchTravelDurationDistance(
        fromLat: String,
        fromLong: String,
        toLat: String,
        toLong: String,
        meansOfTransportation: MeansOfTransportation
    ): TravelDurationDistance {
        try {
            val apiResponse = fetchDirectionsResponse(
                fromLat = fromLat, fromLong = fromLong,
                toLat = toLat, toLong = toLong,
                meansOfTransportation = meansOfTransportation
            )
            val duration: String = secondsToHoursMinutes(apiResponse.routes[0].legs[0].duration)
            val distance: String = (apiResponse.routes[0].legs[0].distance/1000).toString()

            return TravelDurationDistance(
                meansOfTransportation = meansOfTransportation,
                distance = distance,
                duration = String.format("%.2f", duration)
            )
        } catch (e: Exception) {
            //Log.e(logTag,"Failed to calculate route", e)
            return TravelDurationDistance(
                meansOfTransportation = meansOfTransportation,
                distance = "",
                duration = ""
            )
        }
    }

            /*
            Private function that returns api response body
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
            println(path)
            val response: HttpResponse = client.get(path)
            return response.body()
        } catch (e: Exception) {
            //Log.e(logTag, "Error fetching directions", e)
            throw e
        }
    }

    private fun secondsToHoursMinutes (duration: Double): String {
        val hours: Int = duration.toInt() / 3600
        val minutes: Int = (duration.toInt() % 3600) % 60
        return if (hours == 0) {
            "$minutes"
        } else {
            "$hours:$minutes"
        }
    }
}

suspend fun main() {
    val blah = DirectionsDataSource()
    for (entry in MeansOfTransportation.entries) {
        val boh = blah.fetchTravelDurationDistance(
            fromLat = 59.9437354.toString(),
            fromLong = 10.718393.toString(),
            toLat = 59.9201.toString(),
            toLong = 10.7374.toString(),
            meansOfTransportation = entry
        )
        println(boh)

    }

}