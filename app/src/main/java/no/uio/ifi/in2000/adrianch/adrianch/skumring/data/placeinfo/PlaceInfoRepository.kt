package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo

import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo

class PlaceInfoRepository {
    val sunriseDataSource = SunriseDataSource()
    val locationDataSource = LocationForecastDataSource()

    suspend fun getPlaceInfo(lat: String, long: String, id: Int = 0): PlaceInfo {
        //contains data for 10 days currently - might change
        val fullForecast = locationDataSource.fetchWeatherData(lat = lat, long = long)
        val forecastGroupedByDate = fullForecast.groupBy { it.time.take(10) }

        forecastGroupedByDate.forEach{
            val sunActivity = sunriseDataSource.fetchSunActivity(
                lat = lat,
                long = long,
                date = it.value[0].time.take(10)
            )

        }

    }
}