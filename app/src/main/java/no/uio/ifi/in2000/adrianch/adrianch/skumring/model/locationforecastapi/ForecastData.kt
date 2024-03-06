package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi

data class ForecastData(
    val instant: Instant,
    val next_12_hours: Next12Hours,
    val next_1_hours: Next1Hours,
    val next_6_hours: Next6Hours
)