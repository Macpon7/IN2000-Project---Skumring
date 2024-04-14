package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import android.util.Log
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

private const val logTag = "PlaceInfoRepository" //log for error-handling

interface OldPlaceInfoRepository {
    /**
     * Creates and returns a [PlaceInfo] object containing all the details about the place matching the
     * given coordinates and id. If no ID is given, then no name or description can be retrieved, since
     * this means that the coordinates given do not correspond with a place in our database. In this cases
     * the descriptive fields are left blank in the returned object.
     * @param lat String containing the latitude coordinate
     * @param long String containing the longitude coordinate
     * @param id Optional parameter. If id is not 0 then we will fetch details like name and description
     */
    //suspend fun getPlaceInfo(lat: String, long: String, id: Int = 0): PlaceInfo

    /**
     * Makes a list of [DailyEvents] objects, one for each [date][LocalDateTime] key found in the map property.
     *
     * Given a map containing lists of weather forecast data (where the key is the date and the values
     * are the lists), this function creates our simple DailyEvents objects for each date, containing
     * the time of the solar events, and a statement on the conditions for photography at each event.
     */
    suspend fun makeSunEvents(
        forecastGroupedByDate: Map<LocalDate, List<WeatherPerHour>>,
        lat: String, long: String): List<SunEvent>

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
    suspend fun getSunsetString(lat: String, long: String, date: LocalDate): String

    /**
     * Returns the weather conditions during today's sunset given cooridnates
     */
    suspend fun getLocalSunsetWeather (lat: String, long: String) : WeatherPerHour

    /**
     * Returns a 1-3 rating of the given weather conditions
     * @param weatherData
     */
    suspend fun getWeatherConditions(weatherData: WeatherPerHour): WeatherConditions

    }

/**
 * An implementation of [OldPlaceInfoRepository].
 * @property sunriseDataSource instance of [SunriseDataSource]
 * @property locationForecastDataSource instance of [LocationForecastDataSource]
 * @property placeDetailsDataSource instance of [PlaceDetailsDataSource]
 */
class OldPlaceInfoRepositoryImpl (
    private val sunriseDataSource: SunriseDataSource = SunriseDataSource(),
    private val locationForecastDataSource: LocationForecastDataSource = LocationForecastDataSource(),
    private val placeDetailsDataSource: PlaceDetailsDataSource = PlaceDetailsDataSource()
): OldPlaceInfoRepository {
    /*override suspend fun getPlaceInfo(lat: String, long: String, id: Int): PlaceInfo {
        try {

            // Get the forecasted weather at this place
            //contains data for 10 days currently - might change
            val fullForecast = locationForecastDataSource.fetchWeatherData(lat = lat, long = long)

            // Group all the forecast data by date
            val forecastGroupedByDate = fullForecast.groupBy { it.time.toLocalDate() }

            // Send our map to a function which will return a list of DailyEvents
            val sunEventsList = makeSunEvents(
                forecastGroupedByDate = forecastGroupedByDate,
                lat = lat,
                long = long
            )

            val details = placeDetailsDataSource.fetchPlaceDetails(id = id)

            //return PlaceInfo with fetched details
            return PlaceInfo(
                id = id,
                name = details.name,
                description = details.description,
                lat = lat,
                long = long,
                sunEvents = sunEventsList,
            )
        } catch (e: Exception) {
            Log.e(logTag, "Error when fetching placeinfo:" + (e.message ?: ""), e)
            throw e
        }
    }*/

    /**
     * Given a map containing lists of [WeatherPerHour] objects (where the key is a [LocalDate] and
     * the values are the lists), this function creates a [SunEvent] object for each date, containing
     * the time of sunset and a statement on the conditions for photography.
     */
    override suspend fun makeSunEvents(
        forecastGroupedByDate: Map<LocalDate, List<WeatherPerHour>>,
        lat: String,
        long: String
    ): List<SunEvent> {
        // The list we will eventually return
        val sunEventsList = mutableListOf<SunEvent>()

        /* For each date in the map, find out the time of sunset. Then make a new SunEvent object and
        check the conditions, before returning a list. One for each of the next 11 days.
        */
        forecastGroupedByDate.forEach {
            try {
                // Time of sunset as a result of API call to MET Sunrise API
                val sunsetTime: LocalDateTime = sunriseDataSource.fetchSunsetTime(
                    lat = lat,
                    long = long,
                    date = it.key
                )

                // it.value is the list of WeatherPerHour objects for this date
                val sunsetWeather = findClosestWeather(sunsetTime, it.value)

                sunEventsList.add(
                    SunEvent(
                        time = sunsetTime,
                        tempAtEvent = sunsetWeather.instant.air_temperature.toString(),
                        //if icon is null for any reason, use the string "no_icon" instead
                        weatherIcon = sunsetWeather.icon?:"no_icon",
                        conditions = getWeatherConditions(sunsetWeather)
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
     * Given a sunset time of type [LocalDateTime] and list of [WeatherPerHour] objects, this function
     * will return whichever weather object is closest in time to the given sunset time.
     */
    private fun findClosestWeather(sunsetTime: LocalDateTime, weatherData: List<WeatherPerHour>): WeatherPerHour {
        // Pick the first WeatherPerHour object to begin with
        var sunsetWeather: WeatherPerHour = weatherData[0]
        // Then go through all of them and pick the closest to sunset
        weatherData.forEach { forecastObject ->
            // if the time between this WeatherPerHour object and sunsetTime is less than
            // the time between the currently selected closest WeatherPerHour and sunsetTime,
            // pick this WeatherPerHour object as the new closest.
            // Use abs() because the difference int is positive or negative depending on if
            // the time we compare with is before or after sunset
            if (abs(forecastObject.time.compareTo(sunsetTime)) <
                abs(sunsetWeather.time.compareTo(sunsetTime)))
            {
                sunsetWeather = forecastObject
            }
        }

        return sunsetWeather
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

    override suspend fun getSunsetString(lat: String, long: String, date: LocalDate): String {
        try {
            val sunsetDateTime = sunriseDataSource.fetchSunsetTime(lat, long, date)
            return sunsetDateTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
        } catch (e: Exception) {
            Log.e(logTag, "Error processing sunset() data:" + (e.message ?: ""), e)
            throw e
        }
    }

    /**
     * Function for interpreting weather data from a [WeatherPerHour] object and returns a
     * [WeatherConditions] object with descriptions of the weather phenomena relevant for
     * a nice sunset - Cloud coverage in the different layers and humidity.
     */
    override suspend fun getWeatherConditions(weatherData: WeatherPerHour): WeatherConditions {
        try {
            val cloudConditionLow =
                interpretCloudCondition(weatherData.instant.cloud_area_fraction_low)
            val cloudConditionMedium =
                interpretCloudCondition(weatherData.instant.cloud_area_fraction_medium)
            val cloudConditionHigh =
                interpretCloudCondition(weatherData.instant.cloud_area_fraction_high)
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
        } catch (e: Exception) {
            Log.e(logTag, "Error getting weather conditions", e)
            throw e
        }
    }

    /**
     * Cloudy is when cloudAreaFraction > 67.0
     * Fair is when < 33.0 cloudAreaFraction < 67.0
     * Clear is when cloudAreaFraction < 33.0
     * @param cloudAreaFraction Percentage of satellite photo judged to be clouds
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
     * @param humidity Relative humidity in %
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
     * @param cloudConditionLow Coverage of low, rainy clouds - Hindering view of sunset
     * @param cloudConditionMedium Coverage of medium altitude clouds - A nice balance of which gives a nice canvas
     * @param cloudConditionHigh Coverage of high-altitude, whispy clouds - Nice balance gives nice background
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
            rating > 30 -> WeatherConditionsRating.POOR
            rating > 20 -> WeatherConditionsRating.DECENT
            else -> WeatherConditionsRating.EXCELLENT
        }
    }
    override suspend fun getLocalSunsetWeather (lat: String, long: String) : WeatherPerHour {
        val weather = locationForecastDataSource.fetchWeatherData(lat = lat, long = long).groupBy { it.time.toLocalDate() }
        val weatherToday = weather.entries.first()

        val sunsetTime: LocalDateTime = sunriseDataSource.fetchSunsetTime(
            lat = lat,
            long = long,
            date = weatherToday.key
        )

        return findClosestWeather(sunsetTime = sunsetTime, weatherData = weatherToday.value)
    }
}