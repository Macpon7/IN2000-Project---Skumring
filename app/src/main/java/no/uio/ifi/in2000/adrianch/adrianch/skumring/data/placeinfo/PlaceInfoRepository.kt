package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.DailyEvents
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.SunActivity

interface PlaceInfoRepository {
    suspend fun getPlaceInfo(lat: String, long: String, id: Int = 0): PlaceInfo
    suspend fun checkConditions(weatherData: List<WeatherPerHour>): Boolean
}
class PlaceInfoRepositoryImpl (
    private val sunriseDataSource: SunriseDataSource = SunriseDataSource(),
    private val locationDataSource: LocationForecastDataSource = LocationForecastDataSource()
    //private val placeDetailsDataSource: PlaceDetailsDataSource = PlaceDetailsDataSource()
) {
    suspend fun getPlaceInfo(lat: String, long: String, id: Int = 0): PlaceInfo {
        // Get the forecasted weather at this place
        //contains data for 10 days currently - might change
        val fullForecast = locationDataSource.fetchWeatherData(lat = lat, long = long)

        // Group all the forecast data by date
        val forecastGroupedByDate = fullForecast.groupBy { it.time.toLocalDate() }

        /* For each date we have forecast data on, find out the time of sunset/rise
           For each sunset/rise, make a new SunEvent object and check if the conditions will be good
           Make a DailyEvents object for each date and add that to our sunEventsList
        */
        val sunEventsList: MutableList<DailyEvents> = emptyList<DailyEvents>().toMutableList()
        forecastGroupedByDate.forEach{
            // SunActivity is a list of times. One for each event we are interested in
            // In MVP this is only sunset, but we will add sunrise at a later date
            val sunActivity: SunActivity = sunriseDataSource.fetchSunActivity(
                lat = lat,
                long = long,
                date = it.key
            )

            // Filters away all the WeatherPerHour objects whose time is too far from sunset
            val sunsetWeather = it.value.filter {
                //TODO: return true in here if the time of the WeatherPerHour object is the same as sunset +- 1 hour
                true
            }

            //TODO: actually check the conditions using the list of WeatherPerHour objects

            sunEventsList.add(
                DailyEvents(
                    sunset = SunEvent(
                        time = sunActivity.sunset,
                        conditions = true
                    )
                )
            )
        }

        return PlaceInfo(
            name = "",
            description = "",
            latitude = lat,
            longitude = long,
            sunEvents = sunEventsList.toList()
        )
    }

    /**
     * Takes list of WeatherPerHour objects, goes through each of them and
     * checks instant.cloud_area_fraction of a given timestamp. If any of them
     * are above a certain threshold (arbitrarily chosen to be 25  %), it will
     * return "False" as we deem it to be too cloudy. If all timestamps are below,
     * we deem conditions to be good enough.
     */
    fun checkConditions(weatherData: List<WeatherPerHour>): Boolean {
        weatherData.forEach {
            // cloudAreaFraction is the percentage of pixels in a satellite photo
            // over an area judged to be clouds.
            val cloudAreaFraction: Float = it.instant.cloud_area_fraction.toFloat()
            if (cloudAreaFraction > 25) {
                return false
            }
        }
        return true
    }
}