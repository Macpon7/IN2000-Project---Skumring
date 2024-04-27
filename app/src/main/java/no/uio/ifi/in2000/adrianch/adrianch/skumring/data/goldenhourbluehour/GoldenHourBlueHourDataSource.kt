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

    private suspend fun fetchSunriseSunset(path: String): SunriseSunset {
        try {
            val response: HttpResponse = client.get(path)
            return response.body()
        } catch (e: Exception) {
            Log.e(logTag, "Error fetching Golden Hour/Blue Hour info", e)
            throw e
        }
    }

    suspend fun fetchGoldenHourBlueHourTime(lat: String, long: String, date: LocalDate): GoldenHourBlueHour {
        val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        // Fetches info for Central European TZ
        val path = "https://api.sunrisesunset.io/json?lat=$lat&lng=$long&timezone=CET&$dateString"
        val response = fetchSunriseSunset(path)
        val goldenHourTime: String = dateString + " " + fixTimeFormat(response.results.golden_hour)
        val blueHourTime: String = dateString + " " + fixTimeFormat(response.results.dusk)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
        val goldenHourTimeFormatted = LocalDateTime.parse(goldenHourTime, formatter)
        val blueHourTimeFormatted = LocalDateTime.parse(blueHourTime, formatter)
        Log.d(logTag, "Golden hour: ${goldenHourTimeFormatted}, Blue hour: ${blueHourTimeFormatted}")
        return GoldenHourBlueHour(goldenHour = goldenHourTimeFormatted, blueHour = blueHourTimeFormatted)
    }

    private fun fixTimeFormat(time: String): String {
        return if (time[1] == ':') {
            "0$time"
        } else {
            time
        }
    }
}