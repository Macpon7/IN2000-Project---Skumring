package no.uio.ifi.in2000.adrianch.adrianch.skumring.geocoding
import com.google.gson.Gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding.GeocodingDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ReverseGeocoding
import org.junit.Test

class GeoCodingDataSourceTest {

    private val gson = Gson()
    private val source: GeocodingDataSource = GeocodingDataSource()

    private val ojdReverseResponseData: ReverseGeocoding = gson.fromJson(
        ojdReverseGeo,
        ReverseGeocoding::class.java
    )

    /*
    Testing that a response gets converted correctly to our custom object and gets
    the city correct.
     */
    @Test
    fun checkGeocodeCity() {
        val expected = "Oslo"
        val response = source.convertReverseGeocodingResponse(ojdReverseResponseData)
        val result = response.placeName

        assert(expected == result)
    }
}