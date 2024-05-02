package no.uio.ifi.in2000.adrianch.adrianch.skumring.goldenhourbluehour

import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.goldenhourbluehour.GoldenHourBlueHourDataSource
import org.junit.Test

class GoldenHourBlueHourDataSourceTest {

    val goldenHourBlueHourDataSource = GoldenHourBlueHourDataSource()

    @Test
    fun checkCorrectTimeFormatFixTimeFormat() {
        val expected = "11:20 PM"

    }
}