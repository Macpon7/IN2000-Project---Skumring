package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.goldenhourbluehour

import java.time.LocalDate
import java.time.LocalDateTime

data class GoldenHourBlueHour(
    val goldenHour: LocalDateTime,
    val blueHour: LocalDateTime,
)

data class SunriseSunset(
    val results: Results,
    val status: String
)

data class Results(
    val date: String,
    val dawn: String,
    val day_length: String,
    val dusk: String,
    val first_light: String,
    val golden_hour: String,
    val last_light: String,
    val solar_noon: String,
    val sunrise: String,
    val sunset: String,
    val timezone: String,
    val utc_offset: Int
)