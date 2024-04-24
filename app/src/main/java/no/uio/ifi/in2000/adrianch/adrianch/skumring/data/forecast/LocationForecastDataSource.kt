package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.forecast

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.LocationForecastInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherPerHour
import java.time.LocalDateTime

// Constants for logging and API request path
private const val TAG: String = "LocationForecastDatasource"
private const val API_PATH: String =
    "https://api.met.no/weatherapi/locationforecast/2.0/edr/collections/complete/position?"

// Our own HTTP exception type
class HTTPException(override val message: String? = null, override val cause: Throwable? = null) :
    Exception()

class LocationForecastDataSource {

    // HTTP client to send requests, set to receive json content
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    /**
     * Gets the forecast for the specified coordinates from MET API, and converts the response data to
     * our own WeatherPerHour objects
     */
    suspend fun fetchWeatherData(lat: String, long: String): List<WeatherPerHour> {
        try {
            val path = API_PATH + "coords=POINT($long+$lat)"

            val response: HttpResponse = client.get(path) {
                this.headers.append(
                    name = "User-Agent",
                    value = "${R.string.app_name}/${R.string.app_version} (IN2000 prosjekt med Case 5)"
                )
            }

            // If the response code is anything other than 200 OK, throw an HTTPException with the response code as message
            if (response.status != HttpStatusCode.OK) {
                throw HTTPException(
                    message = response.status.toString()
                )
            }

            // Deserialize from JSON to kotlin data class
            val dataFromAPI: LocationForecastInfo = response.body()

            // Convert from API response to a list of WeatherPerHour objects
            return convertResponseToWeatherPerHour(dataFromAPI)
        } catch (e: Exception) { // Handle any type of exception
            Log.e(TAG, "An error occurred in fetchWeatherData", e)
            throw e
        }


    }

    /**
     * Converts the API response into our own WeatherPerHour data classes, discarding all the information
     * in the response that we are not interested in.
     */
    @Suppress("SENSELESS_COMPARISON")
    fun convertResponseToWeatherPerHour(res: LocationForecastInfo): List<WeatherPerHour> {
        return try {
            res.properties.timeseries.map {
                //the next_1_hours object is only available in the short term forecast. After that we need to get the icon from next_6_hours
                val icon: String? = if (it.data.next_1_hours != null) {
                    it.data.next_1_hours.summary.symbol_code
                } else if (it.data.next_6_hours != null) {
                    it.data.next_6_hours.summary.symbol_code
                } else {
                    null
                }
                // We use subsequence of the time string to get rid of the Z character at the end. Other than that Z,
                // The string we get from the API is in ISO format
                WeatherPerHour(
                    time = LocalDateTime.parse(it.time.subSequence(0, 19)),
                    instant = it.data.instant.details,
                    icon = icon
                )
            }
        } catch (e: Exception) {
            // Check for any exceptions when the loop is done
            Log.e(
                TAG,
                "An unexpected in the outer-loop of converterResponseToWeatherPerHour: ${e.message}",
                e
            )
            throw e
        }
    }
}

