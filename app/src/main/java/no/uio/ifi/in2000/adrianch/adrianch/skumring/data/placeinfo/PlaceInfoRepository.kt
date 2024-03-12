package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi.WeatherPerHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.DailyEvents
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi.SunActivity

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

    suspend fun checkConditions(weatherData: List<WeatherPerHour>): Boolean {
        return true
    }
}