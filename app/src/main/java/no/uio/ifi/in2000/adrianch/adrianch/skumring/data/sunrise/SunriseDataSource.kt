package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import io.ktor.utils.io.errors.IOException
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.SunActivity
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.SunriseInfo
import org.json.JSONException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


class SunriseDataSource() {

    // Constant for logging errors:
    private val logTag : String = ""

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
            val response: HttpResponse = client.get(path)
            return response.body()
        } catch (e: IOException) {
            Log.e(logTag, "Network error: ${e.message} in fetchSunriseData", e)
            // Handle network error, e.g. return a default value or specific error code
            throw e
        }catch (e: JSONException) { // Handle error in JSON-parsing
            Log.e(logTag, "An error with handling JSON-parsing: ${e.message} in fetchSunriseData", e)
            throw e
        } catch (e: NullPointerException) { // Handle NullPointerException -> null values from the API
            Log.e(logTag, "An error with null values from the API: ${e.message}, in fetchSunriseData", e)
            throw e
        }catch (e: RuntimeException) {
            Log.e(logTag, "Runtime exception: ${e.message} in fetchSunriseData", e)
            throw e
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            Log.e(logTag, "Error redirect response: ${e.response.status.description} in fetchSunriseData" , e)
            throw e
        }catch (e: ClientRequestException) {
            //4xx - responses
            Log.e(logTag, "Error with client request: ${e.response.status.description} in fetchSunriseData" , e)
            throw e
        }catch (e: ServerResponseException) {
            //5xx - responses
            Log.e(logTag, "Error with server response: ${e.response.status.description} in fetchSunriseData" , e)
            throw e
        }catch (e: Exception) {
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
    suspend fun fetchSunActivity(lat: String, long: String, date: LocalDate): SunActivity {
        try {
            val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val path = "https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%28${long}%20${lat}%29&datetime=${dateString}"
            val response = fetchSunriseData(path)
            val sunsetTime: String = response.properties.sunset.time  //output: 2024-03-07T17:03+00:00
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME //Offset is in timezone 0, so add 1 hour in the line below
            val timeFormatted = LocalDateTime.parse(sunsetTime, formatter).plusHours(1)

            return SunActivity(timeFormatted)
        } catch (e: NoSuchElementException) {
            Log.e(logTag, "Element not found: ${e.message} in fetchSunActivity", e)
            throw e
        } catch (e: DateTimeParseException) {
            Log.e(logTag, "Failed to parse date-time: ${e.message} in fetchSunActivity", e)
            throw e
        } catch (e: IOException) {
            Log.e(logTag, "Network exception: ${e.message} in fetchSunActivity", e)
            throw e
        } catch (e: RuntimeException) {
            Log.e(logTag, "Runtime exception: ${e.message} in fetchSunActivity", e)
            throw e
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            Log.e(logTag, "Error redirect response: ${e.response.status.description} in fetchSunActivity" , e)
            throw e
        }catch (e: ClientRequestException) {
            //4xx - responses
            Log.e(logTag, "Error with client request: ${e.response.status.description} in fetchSunActivity" , e)
            throw e
        }catch (e: ServerResponseException) {
            //5xx - responses
            Log.e(logTag, "Error with server response: ${e.response.status.description} in fetchSunActivity" , e)
            throw e }
        catch (e: Exception) {
            Log.e(logTag, "An unexpected error: ${e.message} in fetchSunActivity", e)
            throw e
        }
    }
}

//Test that should be moved to a test file

/*
suspend fun main() {
    val source = SunriseDataSource()
    //var test_date = "2024-03-07"
    var test_date = LocalDate.of(2023,9,8)
    val fetcher = source.fetchSunriseData("https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%2810%2060%29&datetime=2024-03-07")

    print(fetcher)
}
 */