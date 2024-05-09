package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.SunriseInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Constant for logging:
private const val logTag : String = "SunriseDataSource"

class SunriseDataSource() {
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    /**
     * Sends GET request to API and returns response body
     */
    suspend fun fetchSunriseData(path: String): SunriseInfo {
        try {
            val response: HttpResponse = client.get(path) {
                this.headers.append(
                    name = "User-Agent",
                    value = "${R.string.app_name}/${R.string.app_version} (IN2000 prosjekt med Case 5)"
                )
            }
            return response.body()
        } catch (e: Exception) {
            Log.e(logTag, "Unexpected error: ${e.message} in fetchSunriseData" , e)
            throw e
        }
    }

    /**
     * Calls MET's Sunrise 3.0 API, with the given latitude, longitude and date.
     * Returns the time of sunset for this date.
     * When time is converted from String to LocalDatTime, add 1 because the string
     * is in timezone 0 and Oslo is in timezone +1
     *
     * Inputs:
     *
     * lat: String - Latitude coordinate
     *
     * long: String - Longitude coordinate
     *
     * date: String - ISO formatted date (YYYY-MM-DD)
     */
    suspend fun fetchSunsetTime(lat: String, long: String, date: LocalDate): LocalDateTime {
        try {
            val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val path = "https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%28${long}%20${lat}%29&datetime=${dateString}"
            val response = fetchSunriseData(path)
            val sunsetTime: String = response.properties.sunset.time  //output: 2024-03-07T17:03+00:00
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME //Offset is in timezone 0, so add 2 hour in the line below (we are in summer time now)
            // When the sun never sets (like in the north of Norway in summer) the response from API will be null
            // We save these sunsets as midnight, jan 1 2000, so it's easy for us to detect in code if the sun will never set
            val timeFormatted = if (sunsetTime == null) {
                LocalDateTime.of(2000, 1, 1, 0, 0)
            } else {
                LocalDateTime.parse(sunsetTime, formatter).plusHours(2)
            }
            Log.d(logTag, timeFormatted.toString())

            return timeFormatted
        } catch (e: Exception) {
            Log.e(logTag, "An unexpected error: ${e.message} in fetchSunActivity", e)
            throw e
        }
    }
}