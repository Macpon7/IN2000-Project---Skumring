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
     * Returnerer et LocationForecastInfo-objekt som har timeseriesobjektene med
     * værdata etter gitt tid og gitte koordinater.
     */
    private suspend fun fetchLocationForecastData(longitude: String, latitude: String): LocationForecastInfo {
        this.path += "coords=POINT($longitude+$latitude)"
         try {
            val response: HttpResponse = client.get(this.path)
            return response.body()
        } catch(e: Exception) {
            throw e
        }
    }

    suspend fun fetchWeatherData(latitude: String, longitude: String): List<WeatherPerHour> {
        val dataFromAPI = fetchLocationForecastData(latitude = latitude, longitude = longitude)

        return dataFromAPI.properties.timeseries.map {
            var icon: String?
            if (it.data.next_1_hours == null) {
                icon = null
            } else {
                icon = it.data.next_1_hours.summary.symbol_code
            }

            WeatherPerHour(it.time, it.data.instant.details, icon)
        }
    }
}

