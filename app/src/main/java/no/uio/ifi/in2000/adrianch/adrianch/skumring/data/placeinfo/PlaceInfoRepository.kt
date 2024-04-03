package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.DailyEvents
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.SunActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

private const val logTag = "PlaceInfoRepository" //log for error-handling

interface PlaceInfoRepository {
    suspend fun getPlaceInfo(lat: String, long: String, id: Int = 0): PlaceInfo
    suspend fun makeDailyEvents(
        forecastGroupedByDate: Map<LocalDate, List<WeatherPerHour>>,
        lat: String, long: String): List<DailyEvents>
    suspend fun checkConditions(weatherData: List<WeatherPerHour>): Boolean
    suspend fun getSunsetLocalDateTime(lat: String, long: String, date: LocalDate): LocalDateTime
    suspend fun convertLocalDatetoString(dateTime: LocalDateTime): String
    suspend fun getSunset(lat: String, long: String, date: LocalDate): String
}

class PlaceInfoRepositoryImpl (
    private val sunriseDataSource: SunriseDataSource = SunriseDataSource(),
    private val locationDataSource: LocationForecastDataSource = LocationForecastDataSource(),
    private val placeDetailsDataSource: PlaceDetailsDataSource = PlaceDetailsDataSource()
): PlaceInfoRepository {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getPlaceInfo(lat: String, long: String, id: Int): PlaceInfo {
        try {

            // Get the forecasted weather at this place
            //contains data for 10 days currently - might change
            val fullForecast = locationDataSource.fetchWeatherData(lat = lat, long = long)

            // Group all the forecast data by date
            val forecastGroupedByDate = fullForecast.groupBy { it.time.toLocalDate() }

            // Send our map to a function which will return a list of DailyEvents
            val dailyEventsList = makeDailyEvents(
                forecastGroupedByDate = forecastGroupedByDate,
                lat = lat,
                long = long
            )

            val details = placeDetailsDataSource.fetchPlaceDetails(id = id)

            //return PlaceInfo with fetched details
            return PlaceInfo(
                name = details.name,
                description = details.description,
                latitude = lat,
                longitude = long,
                sunEvents = dailyEventsList
            )
        } catch (e: Exception) {
            Log.e(logTag, "Error when fetching placeinfo:" + (e.message ?: ""), e)
            throw e
        }
    }


    /**
     * Given a map containing lists of weather forecast data (where the key is the date and the values
     * are the lists), this function creates our simple DailyEvents objects for each date, containing
     * the time of the solar events, and a statement on the conditions for photography at each event
     */
    override suspend fun makeDailyEvents(
        forecastGroupedByDate: Map<LocalDate, List<WeatherPerHour>>,
        lat: String,
        long: String
    ): List<DailyEvents> {
        // The list we will eventually return
        val sunEventsList = mutableListOf<DailyEvents>()

        /* For each date in the map, find out the time of sunset/rise
           For each sunset/rise, make a new SunEvent object and check if the conditions will be good
           Make a DailyEvents object for each date and add that to our sunEventsList
        */
        forecastGroupedByDate.forEach {
            try {
                // SunActivity is a list of times. One for each event we are interested in
                // In MVP this is only sunset, but we will add sunrise at a later date
                val sunActivity: SunActivity = sunriseDataSource.fetchSunActivity(
                    lat = lat,
                    long = long,
                    date = it.key
                )

                // Filters away all the WeatherPerHour objects whose time is too far from sunset
                var sunsetWeather: List<WeatherPerHour> = emptyList()
                if (it.value.size == 24) {
                    sunsetWeather = it.value.filter { forecastObject ->
                        abs(sunActivity.sunset.hour - forecastObject.time.hour) < 2
                    }
                } else {
                    var closestForecast: WeatherPerHour = it.value[0]
                    it.value.forEach { forecastObject ->
                        if (abs(sunActivity.sunset.hour - forecastObject.time.hour) < abs(
                                sunActivity.sunset.hour - closestForecast.time.hour
                            )
                        ) {
                            closestForecast = forecastObject
                        }
                    }
                    sunsetWeather = listOf<WeatherPerHour>(closestForecast)
                }

                val sunriseWeather = it.value.filter {
                    //TODO after MVP: filter sunriseWeather as well
                    true
                }

                sunEventsList.add(
                    DailyEvents(
                        sunset = SunEvent(
                            time = sunActivity.sunset,
                            conditions = checkConditions(sunsetWeather)
                        ),
                        /*
                    sunrise = SunEvent(
                        time = sunActivity.sunrise,
                        conditions = checkConditions(sunriseWeather)
                     */
                    )
                )
            } catch (e: Exception) {
                Log.e(logTag, "Error fetching sun activity:" + (e.message ?: ""), e)
                throw e
            }
        }
        return sunEventsList.toList()
    }

    /**
     * Takes list of WeatherPerHour objects, goes through each of them and
     * checks instant.cloud_area_fraction of a given timestamp. If any of them
     * are above a certain threshold (arbitrarily chosen to be 25  %), it will
     * return "False" as we deem it to be too cloudy. If all timestamps are below,
     * we deem conditions to be good enough.
     */

    override suspend fun checkConditions(weatherData: List<WeatherPerHour>): Boolean {
            val cloudAreaFractionThreshold: Float = 70.0F
            weatherData.forEach {

                // cloudAreaFraction is the percentage of pixels in a satellite photo
                // over an area judged to be clouds.
                val cloudAreaFraction: Float = it.instant.cloud_area_fraction.toFloat()
                    return false
                }
            //if no cloud area fraction exceeded the threshold, return true
            return true
    }


    //fetchSunActivity(lat: String, long: String, date: LocalDate)
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getSunsetLocalDateTime(
        lat: String,
        long: String,
        date: LocalDate
    ): LocalDateTime {
            return sunriseDataSource.fetchSunActivity(lat, long, date).sunset
    }

    override suspend fun convertLocalDatetoString(dateTime: LocalDateTime): String {
            val ISO_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE
            return dateTime.format(ISO_FORMATTER)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun getSunset(lat: String, long: String, date: LocalDate): String {
        try {
            return convertLocalDatetoString(getSunsetLocalDateTime(lat, long, date))
        } catch (e: Exception) {
            Log.e(logTag, "Error processing sunset() data:" + (e.message ?: ""), e)
            throw e
        }
    }
}