package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise



import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi.SunActivity
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi.SunriseInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
    suspend fun fetchSunActivity(lat: String, long: String, date: String): SunActivity {
        val path = "https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%28${lat}%20${long}%29&datetime=${date}"
        val response = fetchSunriseData(path)
        val sunset_time: String = response.properties.sunset.time  //output: 2024-03-07T17:03+00:00
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME //Offsett is in timezone 0 so add 1 in line below
        val time_formatted = LocalDateTime.parse(sunset_time, formatter).plusHours(1)

        return SunActivity(time_formatted)
    }
}

//Test that should be moved to a test file
/*
suspend fun main() {
    val source = SunriseDataSource()
    var test_date = "2024-03-07"
    val fetcher = source.fetchSunActivity("10", "60", test_date)
}
 */