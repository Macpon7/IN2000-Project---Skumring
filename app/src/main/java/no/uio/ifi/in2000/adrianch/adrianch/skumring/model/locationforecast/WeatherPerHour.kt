package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast

import java.time.LocalDateTime

data class WeatherPerHour (
    val time: LocalDateTime,
    val instant: WeatherDetails,
    val icon: String?
)
