package no.uio.ifi.in2000.adrianch.adrianch.skumring.locationforecast

import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.LocationForecastInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour
import org.junit.Test
import java.time.LocalDateTime

class PlaceInfoRepositoryTest {

    @Test
    fun checkingIfWeatherConditionsAreGood() {
        val gson = Gson()

        val decodedResponse: LocationForecastInfo = gson.fromJson(
            shortTestData,
            LocationForecastInfo::class.java)

    }
}