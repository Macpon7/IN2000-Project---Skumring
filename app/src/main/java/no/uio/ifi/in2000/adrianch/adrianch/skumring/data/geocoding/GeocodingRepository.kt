package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ReverseGeocodeLocation

private const val logTag = "GeocodingRepository"

interface GeocodingRepository {
    suspend fun getPlaceNameFromCoordinates(
        lat: String, long: String
    ): ReverseGeocodeLocation
}

class GeocodingRepositoryImpl(
    private val geocodingDataSource: GeocodingDataSource = GeocodingDataSource()
): GeocodingRepository {
    override suspend fun getPlaceNameFromCoordinates(
        lat: String,
        long: String
    ): ReverseGeocodeLocation {
        return geocodingDataSource.fetchReverseGeocodeLocation(lat = lat, long = long)
    }
}