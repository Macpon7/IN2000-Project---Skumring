package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import java.net.UnknownHostException
import io.ktor.serialization.gson.gson
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.LocationForecastInfo
import java.io.File
import kotlin.reflect.jvm.internal.impl.serialization.deserialization.FlexibleTypeDeserializer.ThrowException


class LocationForecastDataSource (private var path: String = "https://api.met.no/weatherapi/locationforecast/2.0/edr/collections/complete/position?"){
    private val client = HttpClient {
        install(ContentNegotiation) {
            gson()
        }
    }

    /**
     * Returnerer et LocationForecastInfo-objekt som har timeseriesobjektene med
     * værdata etter gitt tid og gitte koordinater.
     */
    suspend fun fetchLocationForecastData(longitude: String, latitude: String): LocationForecastInfo {
        this.path += "coords=POINT($longitude+$latitude)"
         try {
            val response: HttpResponse = client.get(this.path)
            return response.body()
        } catch(e: Exception) {
            throw e
        }
    }
}
