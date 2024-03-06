package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi

data class WeatherPerHour (
    val time: String,
    val instant: WeatherDetails,
    val icon: String
)
