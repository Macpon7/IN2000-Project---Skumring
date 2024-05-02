package no.uio.ifi.in2000.adrianch.adrianch.skumring.goldenhourbluehour

import com.google.gson.Gson
import com.google.gson.GsonBuilder
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


    @Test
    fun checkFixTimeFormat() {
        val formatter = DateTimeFormatter.ofPattern("HH':'mm")

        val expected = "13:26"
        val responseDateTime = source.convertResponseToGoldenHourBlueHour(osloTestData, dummyDate).goldenHour
        val result = responseDateTime.format(formatter)
        assert(expected == result)
    }
}