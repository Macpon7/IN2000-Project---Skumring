package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.GeocodeLocation

interface GeocodingRepository {
    suspend fun getPlaceNameFromCoordinates(
        lat: String, long: String
    ): GeocodeLocation
    suspend fun getCoordinatesFromAddress(address: String): List<GeocodeLocation>
}

/**
 * Implementation of a [GeocodingRepository] that lets you return a  [GeocodeLocation]
 * object containing a places' coordinates and hopefully name.
 */
class GeocodingRepositoryImpl(
    private val geocodingDataSource: GeocodingDataSource = GeocodingDataSource()
): GeocodingRepository {
    /**
     * Function that lets viewmodels fetch a places name based on cooridnates.
     *
     * [GeocodeLocation] contains lat, long and place name in form of strings.
     */
    override suspend fun getPlaceNameFromCoordinates(
        lat: String,
        long: String
    ): GeocodeLocation {
        return geocodingDataSource.fetchReverseGeocodeLocation(lat = lat, long = long)
    }

    /**
     * Using the Mapbox Geocode API, we send in an address string given by the user and get
     * coordinates matching that address back from Mapbox
     */
    override suspend fun getCoordinatesFromAddress(address: String): List<GeocodeLocation> {
        return geocodingDataSource.fetchForwardGeocodeLocation(address = address)
    }
}