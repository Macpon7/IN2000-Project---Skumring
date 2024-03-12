package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi.LocationForecastInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi.WeatherPerHour


class LocationForecastDataSource (){
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
        this.path += "coords=POINT($long+$lat)"
         try {
            val response: HttpResponse = client.get(this.path)
            return response.body()
        } catch(e: Exception) {
            throw e
        }
    }

    /**
     * Gets the forecast for the specified coordinates from MET API, and converts the response data to
     * our own WeatherPerHour objects
     */
    suspend fun fetchWeatherData(lat: String, long: String): List<WeatherPerHour> {
        val dataFromAPI = fetchLocationForecastData(lat = lat, long = long)
        return convertResponseToWeatherPerHour(dataFromAPI)
    }

    /**
     * Converts the API response into our own WeatherPerHour data classes, discarding all the information
     * in the response that we are not interested in.
     */
    fun convertResponseToWeatherPerHour(res: LocationForecastInfo): List<WeatherPerHour> {
        return res.properties.timeseries.map {
            //the next_1_hours object is only available in the short term forecast. After that we need to get the icon from next_6_hours
            var icon: String? = if (it.data.next_1_hours != null) {
                it.data.next_1_hours.summary.symbol_code
            } else if (it.data.next_6_hours != null) {
                it.data.next_6_hours.summary.symbol_code
            } else {
                null
            }
            WeatherPerHour(it.time, it.data.instant.details, icon)
        }
    }
}

