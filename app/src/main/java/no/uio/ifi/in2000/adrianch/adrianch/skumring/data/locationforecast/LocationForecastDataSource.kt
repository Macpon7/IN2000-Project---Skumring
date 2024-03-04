package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast
import android.util.Log
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import java.net.UnknownHostException
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.LocationForecastInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.Properties
import java.io.File


class LocationForecastDataSource (private val path: String = "https://api.met.no/weatherapi/locationforecast/2.0/edr/collections/complete/position?coords=POINT%2810%2060%29"){
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    /*
    TODO{    Bryte opp URL-en basert på hva man trenger å gi mot hva bruker vil ha   }
    */

    /**
     * Returnerer et LocationForecastInfo-objekt som har timeseriesobjektene med
     * værdata etter gitt tid og gitte koordinater.
     */
    suspend fun fetchLocationForecastData(): LocationForecastInfo {
        val response: HttpResponse = client.get(path)
        return response.body()
    }
}

/**
 * Testfunksjon som printer alle timestamps til APIen
 */
//suspend fun main() {
//    val forecast = LocationForecastDataSource()
//    val response = forecast.fetchLocationForecastData()
//    val ts = response.properties.timeseries
//    ts.forEach { elem ->
//        println(elem.time)
//    }
//}