package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.directions

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.MeansOfTransportation
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.TravelDurationDistance

private const val logTag = "DirectionsRepository"

interface DirectionsRepository {
    suspend fun getTravelDurationDistance(
        fromLat: String,
        fromLong: String,
        toLat: String,
        toLong: String,
        meansOfTransportation: MeansOfTransportation
    ): TravelDurationDistance
}

class DirectionsRepositoryImpl(
    private val directionsDataSource: DirectionsDataSource = DirectionsDataSource()
): DirectionsRepository {
    override suspend fun getTravelDurationDistance(
        fromLat: String,
        fromLong: String,
        toLat: String,
        toLong: String,
        meansOfTransportation: MeansOfTransportation
    ):  TravelDurationDistance {
        return directionsDataSource.fetchTravelDurationDistance(
            fromLat = fromLat,
            fromLong = fromLong,
            toLat = toLat,
            toLong = toLong,
            meansOfTransportation = meansOfTransportation
        )
    }
}