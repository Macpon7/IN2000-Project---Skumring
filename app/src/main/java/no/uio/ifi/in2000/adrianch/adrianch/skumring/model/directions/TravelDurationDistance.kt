package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions

// Distance and duration are empty if it fails to load waypoints
// Use string templates to handle
data class TravelDurationDistance(
    val meansOfTransportation: MeansOfTransportation,
    val distance: String,
    val durationHours: String,
    val durationMinutes: String,
)
