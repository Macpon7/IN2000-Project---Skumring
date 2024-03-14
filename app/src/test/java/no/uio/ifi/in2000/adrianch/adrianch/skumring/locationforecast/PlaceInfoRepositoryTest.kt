package no.uio.ifi.in2000.adrianch.adrianch.skumring.locationforecast

import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.LocationForecastInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour
import org.junit.Test
import java.time.LocalDateTime

class PlaceInfoRepositoryTest {

    @Test
    fun checkingIfWeatherConditionsAreGood() {
        val gson = Gson()

        val badWeatherResponse: LocationForecastInfo = gson.fromJson(
            badWeatherTestData,
            LocationForecastInfo::class.java
        )
        val goodWeatherResponse: LocationForecastInfo = gson.fromJson(
            goodWeatherTestData,
            LocationForecastInfo::class.java
        )

        val source = LocationForecastDataSource()
        val goodResult = source.convertResponseToWeatherPerHour(goodWeatherResponse)
        val badResult = source.convertResponseToWeatherPerHour(badWeatherResponse)
        val testRepo = PlaceInfoRepositoryImpl()

        val goodBoolean = testRepo.checkConditions(goodResult)
        val badBoolean = testRepo.checkConditions(badResult)

        val badExpected = false
        val goodExpected = true

        assert(badBoolean == badExpected)
        assert(goodBoolean == goodExpected)


    }
}