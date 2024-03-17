package no.uio.ifi.in2000.adrianch.adrianch.skumring

import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class SunriseDataSourceTest {
    @Test
    suspend fun APIfetchedcorreclty() {
        val gson = Gson()
        val source = SunriseDataSource()
        val path = "https://api.met.no/weatherapi/sunrise/3.0/edr/collections/sun/position?coords=POINT%2810%2060%29&datetime=2024-03-07"
        val response = source.fetchSunriseData(path)
        assertEquals(4, 2 + 2)
    }
}