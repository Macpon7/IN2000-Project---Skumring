package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast
import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.gson.gson
import io.ktor.utils.io.errors.IOException
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.LocationForecastInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour
import org.json.JSONException
import java.time.LocalDateTime


class LocationForecastDataSource (){

    // Constant for logging errors:
    private val logTag : String = ""

    private var path: String = "https://api.met.no/weatherapi/locationforecast/2.0/edr/collections/complete/position?"
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    /**
     * Sends an http request to the locationforecast API, converts the JSON response to a LocationForecastInfo object and returns it
     */
    private suspend fun fetchLocationForecastData(long: String, lat: String): LocationForecastInfo {
        val newpath = this.path + "coords=POINT($long+$lat)"
        try {
            val response: HttpResponse = client.get(newpath)
            return response.body()
        } catch (e: IOException) {
            Log.e(logTag, "Network error: ${e.message}", e)
            // Handle network error, e.g. return a default value or specific error code
            throw e
        }catch (e: Exception) {
            Log.e(logTag, e.message, e)
            throw e
        }
    }

    /**
     * Gets the forecast for the specified coordinates from MET API, and converts the response data to
     * our own WeatherPerHour objects
     */
    suspend fun fetchWeatherData(lat: String, long: String): List<WeatherPerHour> {
        return try {
            val dataFromAPI = fetchLocationForecastData(lat = lat, long = long)
            convertResponseToWeatherPerHour(dataFromAPI)
        } catch (e: IOException) { // Handle network or connection error
            Log.e(logTag, "An error with network or connection with fetchWeatherData", e)
            throw e
        } catch (e: JSONException) { // Handle error in JSON-parsing
            Log.e(logTag, e.message, e)
            throw e
        } catch (e: NullPointerException) { // Handle NullPointerException -> null values from the API
            Log.e(logTag, e.message, e)
            throw e
        } catch (e: Exception) { // Handle any type of exception
            Log.e(logTag, e.message, e)
            throw e
        }
    }

    /**
     * Converts the API response into our own WeatherPerHour data classes, discarding all the information
     * in the response that we are not interested in.
     */
    fun convertResponseToWeatherPerHour(res: LocationForecastInfo): List<WeatherPerHour> {
        // A list of the weatherPerHour-objects:
        val weatherPerHourList = mutableListOf<WeatherPerHour>()

        try {
            res.properties.timeseries.map {
                var icon: String? = null

                try {
                    icon = if (it.data.next_1_hours != null) {
                        it.data.next_1_hours.summary.symbol_code
                    } else if (it.data.next_6_hours != null) {
                        it.data.next_6_hours.summary.symbol_code
                    } else {
                        null
                    }

                    // We use subsequence of the time string to get rid of the Z character at the end. Other than that Z,
                    // The string we get from the api is in ISO format
                    val dateTime = LocalDateTime.parse(it.time.subSequence(0, 19))
                    val weatherPerHour = WeatherPerHour(dateTime, it.data.instant.details, icon)

                    // Add the weatherPerHour object to the list:
                    weatherPerHourList.add(weatherPerHour)
                } catch (e: Exception) {
                    // Check for any exceptions with each loop of the data
                    Log.e(logTag, e.message, e)
                    throw e                }
            }
        } catch (e: Exception) {
            // Check for any exceptions when the loop is done
            Log.e(logTag, e.message, e)
            throw e
        }
        return weatherPerHourList
    }
}
