package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditions
import java.time.LocalDateTime

/**
 * Represents one event (like sunrise or sunset). Contains the exact time of the event happening,
 * the temperature at the event, an icon of what the weather will be like at the event (we use strings
 * to load a specific icon in the UI), and a [WeatherConditions] object which contains information about
 * what the conditions will be like.
 */
data class SunEvent (
    val time: LocalDateTime,
    val goldenHourTime: LocalDateTime,
    val blueHourTime: LocalDateTime,
    val tempAtEvent: String,
    val weatherIcon: String,
    val conditions: WeatherConditions
)