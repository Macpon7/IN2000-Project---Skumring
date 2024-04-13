package no.uio.ifi.in2000.adrianch.adrianch.skumring.placeinforepository

import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.locationforecast.badWeatherTestData
import no.uio.ifi.in2000.adrianch.adrianch.skumring.locationforecast.goodWeatherTestData
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.LocationForecastInfo
import org.junit.Test

class CheckConditionsTest {
    /**
     * Testing the function in PlaceInfoRepository that lets us check if
     * the cloud conditions are adequate for photography.
     * Checks up with manually controlled dummy data if it judges correctly
     * according to the agreed upon conditions.
     */
    @Test
    fun checkingIfWeatherConditionsAreGood() {
        val gson = Gson()

        val goodWeatherResponse: LocationForecastInfo = gson.fromJson(
            goodWeatherTestData,
            LocationForecastInfo::class.java
        )

        val source = LocationForecastDataSource()
        val goodResult = source.convertResponseToWeatherPerHour(goodWeatherResponse)
        val testRepo = OldPlaceInfoRepositoryImpl()

        val goodBoolean = testRepo.checkConditions(goodResult)
        val goodExpected = true

        assert(goodBoolean == goodExpected)
    }

    @Test
    fun checkingIfWeatherConditionsAreBad() {
        val gson = Gson()

        val badWeatherResponse: LocationForecastInfo = gson.fromJson(
            badWeatherTestData,
            LocationForecastInfo::class.java
        )

        val source = LocationForecastDataSource()
        val badResult = source.convertResponseToWeatherPerHour(badWeatherResponse)
        val testRepo = OldPlaceInfoRepositoryImpl()

        val badBoolean = testRepo.checkConditions(badResult)
        val badExpected = false

        assert(badBoolean == badExpected)
    }
}