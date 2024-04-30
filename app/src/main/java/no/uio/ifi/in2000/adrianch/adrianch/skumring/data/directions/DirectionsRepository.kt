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
    suspend fun getAllTravelDurationDistance(
        fromLat: String,
        fromLong: String,
        toLat: String,
        toLong: String,
    ): List<TravelDurationDistance>
}

/**
 * Implementation of a [DirectionsRepository] that fetches a [TravelDurationDistance]
 * object which contains an estimate of the travel time and distance given a certain
 * [MeansOfTransportation].
 */

class DirectionsRepositoryImpl(
    private val directionsDataSource: DirectionsDataSource = DirectionsDataSource()
): DirectionsRepository {

    /**
     * Function that lets viewmodels fetch suggested travel time and distance
     * between two sets of coordinates, given a means of transportation.
     */
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
    override suspend fun getAllTravelDurationDistance(
        fromLat: String,
        fromLong: String,
        toLat: String,
        toLong: String,
    ): List<TravelDurationDistance> {
        val outList = mutableListOf<TravelDurationDistance>()
        MeansOfTransportation.entries.forEach { entry ->
            outList.add(getTravelDurationDistance(
                fromLat = fromLat,
                fromLong = fromLong,
                toLat = toLat,
                toLong = toLong,
                meansOfTransportation = entry
            )
            )
        }
        return outList.toList()
    }


}