package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.goldenhourbluehour

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.goldenhourbluehour.GoldenHourBlueHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.goldenhourbluehour.SunriseSunset
import org.jetbrains.annotations.VisibleForTesting
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val logTag: String = "GHBHDataSource"

class GoldenHourBlueHourDataSource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    /**
     * Gets response body from API that gets us golden and blue hour
     */
    private suspend fun fetchSunriseSunset(path: String): SunriseSunset {
        try {
            val response: HttpResponse = client.get(path)
            return response.body()
        } catch (e: Exception) {
            Log.e(logTag, "Error fetching Golden Hour/Blue Hour info", e)
            throw e
        }
    }

    /**
     * Calls SunriseSunset.io with given latitude, longitude and date and returns
     * golden hour and blue hour for said parameters.
     *
     *
     * If the area checked doesn't have blue/golden hour, it returns an empty string
     * fun formatTime transforms into a dummy LocalDateTime set to 2000-01-01 00:00
     *
     * Inputs:
     *
     * lat: String - Latitude coordinate
     *
     * long: String - Longitude coordinate
     *
     * date: String - ISO formatted date (YYYY-MM-DD)
     */
    suspend fun fetchGoldenHourBlueHourTime(lat: String, long: String, date: LocalDate): GoldenHourBlueHour {
        try {
            val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
            // Fetches info for Central European TZ
            val path = "https://api.sunrisesunset.io/json?lat=$lat&lng=$long&timezone=CET&date=$dateString"
            val response = fetchSunriseSunset(path)

            return convertResponseToGoldenHourBlueHour(response)

        } catch (e: Exception) {
            Log.e(logTag, "Failed fetching Golden/Blue Hour", e)
            throw e
        }

    }
    /*
    Function that does the conversion, separated and made public for tesitng pusposes
     */
    fun convertResponseToGoldenHourBlueHour(
        response: SunriseSunset,
    ): GoldenHourBlueHour {
        val goldenHourDateTime: LocalDateTime = formatTime(
            time = response.results.golden_hour ?: "",
            date = response.results.date)
        val blueHourDateTime: LocalDateTime = formatTime(
            time = response.results.dusk ?: "",
            date = response.results.date)
        Log.d(logTag, "Golden hour: $goldenHourDateTime, Blue hour: $blueHourDateTime")
        return GoldenHourBlueHour(goldenHour = goldenHourDateTime, blueHour = blueHourDateTime)
    }

    /**
     * Auxiliary functions that lets us convert from the format provided by the API
     * to a LocalDateTime object. If it fails to parse the datetime because the time string
     * is empty, it defaults to a dummy DateTime which we will check for later.
     */
    private fun formatTime(time: String, date: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
        val dateTimeString = date + " " + fixTimeFormat(time)
        return try {
            LocalDateTime.parse(dateTimeString, formatter)
        } catch (e: Exception) {
            LocalDateTime.parse("2000-01-01 00:00:00 AM", formatter)
        }
    }

    /**
     * Because the API doesn't format its output time properly we have to do it.
     * Adds a zero in front of single digit hours. If it gets an empty or correctly
     * formatted time string, it will be returned as is.
     */
    private fun fixTimeFormat(time: String): String {
        return try {
            if (time[1] == ':') {
                "0$time"
            } else {
                time
            }
        } catch (e: IndexOutOfBoundsException) {
            time
        }
        catch (e: Exception) {
            Log.e(logTag, "Error handling time", e)
            throw e
        }
    }
}