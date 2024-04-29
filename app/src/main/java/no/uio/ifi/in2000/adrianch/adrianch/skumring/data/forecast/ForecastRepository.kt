package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.forecast

import android.util.Log
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.goldenhourbluehour.GoldenHourBlueHourDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherPerHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.goldenhourbluehour.GoldenHourBlueHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.SunEvent
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.SortedMap
import kotlin.math.abs

private const val TAG = "PlaceInfoRepository" //log for error-handling

interface ForecastRepository {
    suspend fun makeSunEvents(
        forecastGroupedByDate: SortedMap<LocalDate, List<WeatherPerHour>>, lat: String, long: String
    ): List<SunEvent>

    suspend fun findClosestWeather(
        sunsetTime: LocalDateTime, weatherData: List<WeatherPerHour>
    ): WeatherPerHour

    suspend fun getWeatherConditions(weatherData: WeatherPerHour): WeatherConditions
    suspend fun interpretCloudCondition(cloudAreaFraction: Double): CloudConditions
    suspend fun interpretHumidity(humidity: Double): AirConditions
    suspend fun getRating(
        cloudConditionLow: CloudConditions,
        cloudConditionMedium: CloudConditions,
        cloudConditionHigh: CloudConditions,
        airCondition: AirConditions
    ): WeatherConditionsRating
}

/**
 * An implementation of [ForecastRepository].
 * @property sunriseDataSource instance of [SunriseDataSource]
 * @property locationForecastDataSource instance of [LocationForecastDataSource]
 * @property goldenHourBlueHourDataSource instance of [GoldenHourBlueHourDataSource]
 */
class ForecastRepositoryImpl(
    private val sunriseDataSource: SunriseDataSource = SunriseDataSource(),
    private val locationForecastDataSource: LocationForecastDataSource = LocationForecastDataSource(),
    private val goldenHourBlueHourDataSource: GoldenHourBlueHourDataSource = GoldenHourBlueHourDataSource()
) : ForecastRepository {
    /**
     * Given a map containing lists of [WeatherPerHour] objects (where the key is a [LocalDate] and
     * the values are the lists), this function creates a [SunEvent] object for each date, containing
     * the time of sunset and a statement on the conditions for photography.
     */
    override suspend fun makeSunEvents(
        forecastGroupedByDate: SortedMap<LocalDate, List<WeatherPerHour>>, lat: String, long: String
    ): List<SunEvent> {
        // The list we will eventually return
        val sunEventsList = mutableListOf<SunEvent>()

        /* For each date in the map, find out the time of sunset. Then make a new SunEvent object and
        check the conditions, before returning a list. One for each of the next 11 days. */
        forecastGroupedByDate.forEach {
            try {
                // Time of sunset as a result of API call to MET Sunrise API
                val sunsetTime: LocalDateTime = sunriseDataSource.fetchSunsetTime(
                    lat = lat, long = long, date = it.key
                )

                val goldenHourBlueHour: GoldenHourBlueHour = goldenHourBlueHourDataSource.fetchGoldenHourBlueHourTime(
                    lat = lat, long = long, date = it.key)

                // it.value is the list of WeatherPerHour objects for this date
                val sunsetWeather = findClosestWeather(sunsetTime, it.value)

                sunEventsList.add(
                    SunEvent(
                        time = sunsetTime,
                        tempAtEvent = sunsetWeather.instant.air_temperature.toString(),
                        //if icon is null for any reason, use the string "no_icon" instead
                        weatherIcon = sunsetWeather.icon ?: "no_icon",
                        conditions = getWeatherConditions(sunsetWeather),
                        //goldenHourTime = goldenHourBlueHour.goldenHour,
                        //blueHourTime = goldenHourBlueHour.blueHour
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error while making SunEvents", e)
                throw e
            }
        }
        return sunEventsList.toList()
    }

    /**
     * Given a sunset time of type [LocalDateTime] and list of [WeatherPerHour] objects, this function
     * will return whichever weather object is closest in time to the given sunset time.
     */
    override suspend fun findClosestWeather(
        sunsetTime: LocalDateTime, weatherData: List<WeatherPerHour>
    ): WeatherPerHour {
        // Pick the first WeatherPerHour object to begin with
        var sunsetWeather: WeatherPerHour = weatherData[0]
        // Then go through all of them and pick the closest to sunset
        weatherData.forEach {
            // if the time between this WeatherPerHour object and sunsetTime is less than
            // the time between the currently selected closest WeatherPerHour and sunsetTime,
            // pick this WeatherPerHour object as the new closest.
            // Use abs() because the difference int is positive or negative depending on if
            // the time we compare with is before or after sunset
            if (abs(it.time.compareTo(sunsetTime)) < abs(sunsetWeather.time.compareTo(sunsetTime))) {
                sunsetWeather = it
            }
        }

        return sunsetWeather
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
            Log.e(TAG, "Error getting weather conditions", e)
            throw e
        }
    }

    /**
     * Cloudy is when cloudAreaFraction > 67.0
     * Fair is when < 33.0 cloudAreaFraction < 67.0
     * Clear is when cloudAreaFraction < 33.0
     * @param cloudAreaFraction Percentage of satellite photo judged to be clouds
     */
    override suspend fun interpretCloudCondition(cloudAreaFraction: Double): CloudConditions {
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
    override suspend fun interpretHumidity(humidity: Double): AirConditions {
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
     * @param cloudConditionHigh Coverage of high-altitude, wispy clouds - Nice balance gives nice background
     */
    override suspend fun getRating(
        cloudConditionLow: CloudConditions,
        cloudConditionMedium: CloudConditions,
        cloudConditionHigh: CloudConditions,
        airCondition: AirConditions
    ): WeatherConditionsRating {

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
}