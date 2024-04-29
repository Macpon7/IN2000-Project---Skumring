package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.ReverseGeocodeLocation

private const val logTag = "GeocodingRepository"

interface GeocodingRepository {
    /**
     * Function that lets viewmodels fetch a places name based on cooridnates.
     */
    suspend fun getPlaceNameFromCoordinates(
        lat: String, long: String
    ): ReverseGeocodeLocation
}

/**
 * Implementation of a [GeocodingRepository] that lets you return a  [ReverseGeocodeLocation]
 * object containing a places' coordinates and hopefully name.
 */
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