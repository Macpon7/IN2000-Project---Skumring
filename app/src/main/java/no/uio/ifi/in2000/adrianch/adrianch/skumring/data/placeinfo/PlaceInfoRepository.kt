package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.DailyEvents
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.SunActivity
import java.time.LocalDate

interface PlaceInfoRepository {
    suspend fun getPlaceInfo(lat: String, long: String, id: Int = 0): PlaceInfo
    suspend fun makeDailyEvents(
        forecastGroupedByDate: Map<LocalDate, List<WeatherPerHour>>,
        lat: String, long: String): List<DailyEvents>
    suspend fun checkConditions(weatherData: List<WeatherPerHour>): Boolean
}

class PlaceInfoRepositoryImpl (
    private val sunriseDataSource: SunriseDataSource = SunriseDataSource(),
    private val locationDataSource: LocationForecastDataSource = LocationForecastDataSource()
    //private val placeDetailsDataSource: PlaceDetailsDataSource = PlaceDetailsDataSource()
): PlaceInfoRepository {
    override suspend fun getPlaceInfo(lat: String, long: String, id: Int): PlaceInfo {
        // Get the forecasted weather at this place
        //contains data for 10 days currently - might change
        val fullForecast = locationDataSource.fetchWeatherData(lat = lat, long = long)

        // Group all the forecast data by date
        val forecastGroupedByDate = fullForecast.groupBy { it.time.toLocalDate() }

        // Send our map to a function which will return a list of DailyEvents
        val dailyEventsList = makeDailyEvents(forecastGroupedByDate = forecastGroupedByDate, lat = lat, long = long)

        //TODO: get name and description from PlaceDetailsDataSource

        return PlaceInfo(
            name = "",
            description = "",
            latitude = lat,
            longitude = long,
            sunEvents = dailyEventsList
        )
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

            val sunriseWeather = it.value.filter {
                //TODO after MVP: filter sunriseWeather as well
                true
            }

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

        }

        return sunEventsList.toList()
    }

    override suspend fun checkConditions(weatherData: List<WeatherPerHour>): Boolean {
        return true
    }
}