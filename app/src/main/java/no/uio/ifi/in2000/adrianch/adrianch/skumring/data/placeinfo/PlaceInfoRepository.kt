package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.DailyEvents
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.SunActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

private const val logTag = "PlaceInfoRepository" //log for error-handling

interface PlaceInfoRepository {
    /**
     * Creates and returns a [PlaceInfo] object containing all the details about the place matching the
     * given coordinates and id. If no ID is given, then no name or description can be retrieved, since
     * this means that the coordinates given do not correspond with a place in our database. In this cases
     * the descriptive fields are left blank in the returned object.
     * @param lat String containing the latitude coordinate
     * @param long String containing the longitude coordinate
     * @param id Optional parameter. If id is not 0 then we will fetch details like name and description
     */
    suspend fun getPlaceInfo(lat: String, long: String, id: Int = 0): PlaceInfo

    /**
     * Makes a list of [DailyEvents] objects, one for each [date][LocalDateTime] key found in the map property.
     *
     * Given a map containing lists of weather forecast data (where the key is the date and the values
     * are the lists), this function creates our simple DailyEvents objects for each date, containing
     * the time of the solar events, and a statement on the conditions for photography at each event.
     */
    suspend fun makeDailyEvents(
        forecastGroupedByDate: Map<LocalDate, List<WeatherPerHour>>,
        lat: String, long: String): List<DailyEvents>

    /**
     * Takes list of WeatherPerHour objects, goes through each of them and
     * checks instant.cloud_area_fraction of a given timestamp. If any of them
     * are above a certain threshold (arbitrarily chosen to be 25  %), it will
     * return "False" as we deem it to be too cloudy. If all timestamps are below,
     * we deem conditions to be good enough.
     */
    suspend fun checkConditions(weatherData: List<WeatherPerHour>): Boolean

    /**
     * Returns a string containing the time of sunset on the given date at the given coordinates.
     */
    suspend fun getSunset(lat: String, long: String, date: LocalDate): String
}

/**
 * An implementation of [PlaceInfoRepository].
 * @property sunriseDataSource instance of [SunriseDataSource]
 * @property locationDataSource instance of [LocationForecastDataSource]
 * @property placeDetailsDataSource instance of [PlaceDetailsDataSource]
 */
class PlaceInfoRepositoryImpl (
    private val sunriseDataSource: SunriseDataSource = SunriseDataSource(),
    private val locationDataSource: LocationForecastDataSource = LocationForecastDataSource(),
    private val placeDetailsDataSource: PlaceDetailsDataSource = PlaceDetailsDataSource()
): PlaceInfoRepository {
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
     * Takes a list of [WeatherPerHour] objects and checks if any of them expects the cloud
     * coverage in certain layers to be above certain thresholds (30 % for low clouds and
     * 70 % for medium).
     */
    override suspend fun checkConditions(weatherData: List<WeatherPerHour>): Boolean {
        val cloudAreaFractionMediumThreshold = 70.0F
        val cloudAreaFractionLowThreshold = 30.0F
        weatherData.forEach {
            // cloudAreaFraction is the percentage of pixels in a satellite photo
            // over an area judged to be clouds.
            // High and medium clouds allow for nice conditions within certain parameters
            val cloudAreaFractionLow: Float = it.instant.cloud_area_fraction_low.toFloat()
            val cloudAreaFractionMedium: Float = it.instant.cloud_area_fraction_medium.toFloat()
            if (cloudAreaFractionLow > cloudAreaFractionLowThreshold &&
                cloudAreaFractionMedium > cloudAreaFractionMediumThreshold) {
                return false
            }
        }
        return true
    }

    override suspend fun getSunset(lat: String, long: String, date: LocalDate): String{
        try {
            val sunsetDateTime = sunriseDataSource.fetchSunActivity(lat, long, date).sunset
            return sunsetDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE)
        } catch (e: Exception) {
            Log.e(logTag, "Error processing sunset() data:" + (e.message ?: ""), e)
            throw e
        }
    }

    suspend fun getWeatherConditions(weatherData: WeatherPerHour): WeatherConditions {
        val cloudConditionLow = interpretCloudCondition(weatherData.instant.cloud_area_fraction_low)
        val cloudConditionMedium = interpretCloudCondition(weatherData.instant.cloud_area_fraction_medium)
        val cloudConditionHigh = interpretCloudCondition(weatherData.instant.cloud_area_fraction_high)
        val airCondition = interpretHumidity(weatherData.instant.relative_humidity)

        return WeatherConditions(
            weatherRating = getRating(
                cloudConditionLow = cloudConditionLow,
                cloudConditionMedium = cloudConditionMedium,
                cloudConditionHigh = cloudConditionHigh,
                airCondition = airCondition,
            ),
            cloudConditionLow = cloudConditionLow,
            cloudConditionMedium = cloudConditionMedium,
            cloudConditionHigh = cloudConditionHigh,
            airCondition = airCondition
        )
    }

    /**
     * Cloudy is when cloudAreaFraction > 67.0
     * Fair is when < 33.0 cloudAreaFraction < 67.0
     * Clear is when cloudAreaFraction < 33.0
     */
    private fun interpretCloudCondition(cloudAreaFraction: Double): CloudConditions {
        return if (cloudAreaFraction > 67) {
            CloudConditions.CLOUDY
        } else if (cloudAreaFraction > 33) {
            CloudConditions.FAIR
        } else {
            CloudConditions.CLEAR
        }
    }

    /**
     * High is when relative_humidity > 67.0
     * Mid is when < 33.0 relative_humidity < 67.0
     * Low is when relative_humidity < 33.0
     */
    private fun interpretHumidity(humidity: Double): AirConditions {
        return if (humidity > 67) {
            AirConditions.HIGH
        } else if (humidity > 33) {
            AirConditions.MID
        } else {
            AirConditions.LOW
        }
    }

    /**
     * Returns a rating between 1-3 based on humidity and the cloud coverage of the
     * three different altitude layers. 1 is bad, 3 is good.
     */
    private fun getRating(
        cloudConditionLow: CloudConditions,
        cloudConditionMedium: CloudConditions,
        cloudConditionHigh: CloudConditions,
        airCondition: AirConditions): WeatherConditionsRating {

        var rating = 0

        // If there are lots of low-altitude clouds, it'll make for a bad sunset
        rating += when (cloudConditionLow) {
            CloudConditions.CLOUDY -> 30
            CloudConditions.FAIR -> 20
            CloudConditions.CLEAR -> 0
        }

        // Ideally we want it a little cloudy in the mid-high layers
        rating += when (cloudConditionMedium) {
            CloudConditions.CLOUDY -> 10
            CloudConditions.FAIR -> 2
            CloudConditions.CLEAR -> 3
        }

        rating += when (cloudConditionHigh) {
            CloudConditions.CLOUDY -> 5
            CloudConditions.FAIR -> 0
            CloudConditions.CLEAR -> 10
        }

        // Humidity should be low
        rating += when (airCondition) {
            AirConditions.HIGH -> 10
            AirConditions.MID -> 5
            AirConditions.LOW -> 0
        }

        return when {
            rating > 30 -> WeatherConditionsRating.ONE
            rating > 20 -> WeatherConditionsRating.TWO
            else -> WeatherConditionsRating.THREE
        }
    }
}