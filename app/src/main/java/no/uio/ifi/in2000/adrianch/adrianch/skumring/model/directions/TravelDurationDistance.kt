package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions

data class TravelDurationDistance(
    val meansOfTransportation: MeansOfTransportation,
    val distance: Double,
    val duration: String
)
