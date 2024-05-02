package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions

data class Directions(
    val code: String,
    val routes: List<Route>,
    val uuid: String,
    val waypoints: List<Waypoint>
)

data class Admin(
    val iso_3166_1: String,
    val iso_3166_1_alpha3: String
)

data class Geometry(
    val coordinates: List<List<Double>>,
    val type: String
)

data class Leg(
    val admins: List<Admin>,
    val distance: Double,
    val duration: Double,
    val steps: List<Any>,
    val summary: String,
    val via_waypoints: List<Any>,
    val weight: Double
)

data class Route(
    val distance: Double,
    val duration: Double,
    val geometry: Geometry,
    val legs: List<Leg>,
    val weight: Double,
    val weight_name: String
)

data class Waypoint(
    val distance: Double,
    val location: List<Double>,
    val name: String
)