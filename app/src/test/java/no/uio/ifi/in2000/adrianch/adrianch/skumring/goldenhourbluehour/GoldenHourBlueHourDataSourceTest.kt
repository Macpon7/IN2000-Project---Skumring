package no.uio.ifi.in2000.adrianch.adrianch.skumring.goldenhourbluehour

import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.goldenhourbluehour.GoldenHourBlueHourDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.goldenhourbluehour.SunriseSunset
import org.junit.Test
import java.time.format.DateTimeFormatter

class GoldenHourBlueHourDataSourceTest {

    val gson = Gson()

    val source: GoldenHourBlueHourDataSource = GoldenHourBlueHourDataSource()
    val dummyDate: String = "2000-01-01"

    val osloTestData: SunriseSunset = gson.fromJson(
        osloTestCall,
        SunriseSunset::class.java
    )
    val northPoleTestData: SunriseSunset  = gson.fromJson(
        northPoleTestCall,
        SunriseSunset::class.java
    )

    /*
    Testing that a response gets correctly converted to our custom object
    as well as the time getting converted correctly.
     */
    @Test
    fun checkFixTimeFormat() {
        val formatter = DateTimeFormatter.ofPattern("HH':'mm")
        val expected = "13:26"
        val responseDateTime = source.convertResponseToGoldenHourBlueHour(osloTestData).goldenHour
        val result = responseDateTime.format(formatter)

        assert(expected == result)
    }

    /*
    This checks for blue/golden hour at the north pole, but above certain latitudes
    the sun doesn't set during winter, so this would be relevant for locations
    above the polar circle.
     */
    @Test
    fun checkNoGoldenOrBlueHour() {
        val expected = "2000-01-01T00:00"
        val responseDateTime = source.convertResponseToGoldenHourBlueHour(northPoleTestData).blueHour
        val result = responseDateTime.toString()

        assert(expected == result)
    }
}