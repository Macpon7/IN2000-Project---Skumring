package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise


import android.os.Build
import androidx.annotation.RequiresApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi.SunActivity
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi.SunriseInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi.SunsetLDT
import java.time.LocalDateTime


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

    /*
    suspend fun fetchSunActivity(lat: String, long: String, date: LocalDateTime)  {
        val newdate = "2023-03-10"
        val path = "https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%28${lat}%20${long}%29&datetime=${newdate}"
        val response = fetchSunriseData(path)
        //var test_dated = "2007-12-03T10:15:30"

        //val parsedDated = LocalDateTime.parse(test_dated)

        //return SunActivity(response.properties.sunset.time)
    }

     */

    //Skal returnere SunActivity
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchSunActivity(lat: String, long: String, date: String) {
        //val newdate = "2024-03-07"
        val path = "https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%28${lat}%20${long}%29&datetime=${date}"
        val response = fetchSunriseData(path)
        val sunset_azimuth: Double = response.properties.sunset.azimuth
        val sunset_time: String = response.properties.sunset.time  //output: 2024-03-07T17:03+00:00
        val sunsetLDT = SunsetLDT(sunset_azimuth,sunset_time)
        //println(sunsetLDT.datetime)
        println(sunset_time)

        //opprette SunsetLDT object
        //var test_dated = "2007-12-03T10:15:30"
        //val parsedDated = LocalDateTime.parse(test_dated)
        //return SunActivity()
    }
}

//Test that should be moved to a test file
@RequiresApi(Build.VERSION_CODES.O)
suspend fun main() {
    val source = SunriseDataSource()
    var test_date = "2024-03-07"


    val fetcher = source.fetchSunActivity("10", "60", test_date)
}

//Kode som bare er her
//var test_date = "2007-12-03T10:15:30"
//val parsedDate = LocalDateTime.parse(test_date)
//val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")