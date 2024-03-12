package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi

import java.time.LocalDateTime

data class WeatherPerHour (
    val time: LocalDateTime,
    val instant: WeatherDetails,
    val icon: String?
)
