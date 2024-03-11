package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise

import android.icu.text.DateFormat
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi.SunActivity
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi.SunriseInfo




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
        val response: HttpResponse = client.get(path)
        return response.body()
    }

    /**
     * Calls MET's Sunrise 3.0 API, with the given latitude, longitude and date.
     * Returns the time of sunset for this date.
     *
     * Inputs:
     *
     * lat: String - Latitude coordinate
     *
     * long: String - Longitude coordinate
     *
     * date: String - ISO formatted date (YYYY-MM-DD)
     */
    suspend fun fetchSunActivity(lat: String, long: String, date: String?): SunActivity {
        var path: String = "https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%2810%2060%29"
        path = "https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%28${lat}%20${long}%29"

        val response = fetchSunriseData(path)
        return SunActivity(response.properties.sunset.time)
    }
}

//Test that should be moved to a test file
suspend fun main() {
    val source = SunriseDataSource()
    var test_date = "2024-03-10"
    //val sunset = source.fetchSunActivity("10", "60", test_date)
}