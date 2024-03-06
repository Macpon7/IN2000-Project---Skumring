package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi

data class LocationForecastInfo(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)