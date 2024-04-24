package no.uio.ifi.in2000.adrianch.adrianch.skumring

import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.Geometry
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.Properties
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.Solarmidnight
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.Solarnoon
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.Sunrise
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.SunriseInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.Sunset
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunrise.When
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SunriseDataSourceTest {
    val expected = SunriseInfo(
        copyright = "MET Norway",
        licenseURL = "https://api.met.no/license_data.html",
        type = "Feature",

        geometry = Geometry(
            type = "Point",
            coordinates = listOf(10.0f, 60.0f)
        ),
        `when` = When(
            interval = listOf("2024-03-06T23:20:00Z", "2024-03-07T23:30:00Z")
        ),
        properties = Properties(
            body = "Sun",
            solarnoon = Solarnoon(
                visible = true,
                time = "2024-03-07T11:30+00:00",
                discCentreElevation = 0.0
                //discCentreElevation = 25.01
            ),
            sunrise = Sunrise(
                azimuth = 98.73,
                time = "2024-03-07T05:59+00:00"
            ),
            sunset = Sunset(
                azimuth = 261.63,
                time = "2024-03-07T17:03+00:00"
            ),
            solarmidnight = Solarmidnight(
                visible = false,
                time = "2024-03-06T23:31+00:00",
                discCentreElevation = 0.0
                //discCentreElevation = -35.18
            )
        )
    )


    //Checking if FetchSunriseData returns a SunriseInfo object
    @Test
    fun checkReturntypeFetchSunriseData() = runBlocking {
        val source = SunriseDataSource()
        val result = source.fetchSunriseData("https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%2810%2060%29&datetime=2024-03-07")
        assert(expected::class == result::class) { """
        Expected return class: $expected::class
        Result class: $result::class"""
        }
    }

    //Checking if FetchSunriseData returns a SunriseInfo object with right instances of data classes
    @Test
    fun checkdataClassesMatchValuesInJSON() = runBlocking(){
        val source = SunriseDataSource()
        val result = source.fetchSunriseData("https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%2810%2060%29&datetime=2024-03-07")
        assert(expected == result) { """
        Expected return class: $expected
        Result class: $result"""
        }
    }

    /* Not finished
    @Test
    fun checkSameDateSunActivityAsInput() = runBlocking() {
        val source = SunriseDataSource()
        val localDate = LocalDate.of(2023,9,8)

        val sunActivity: SunActivity = source.fetchSunActivity("10","60", localDate)

        assert(expected == result) { """
        Expected return class: $expected
        Result class: $result"""
        }
    }
     */

}