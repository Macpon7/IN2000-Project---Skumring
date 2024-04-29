package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions

// Distance and duration are empty if it fails to load waypoints
data class TravelDurationDistance(
    val meansOfTransportation: MeansOfTransportation,
    val distance: String,
    val duration: String
)
