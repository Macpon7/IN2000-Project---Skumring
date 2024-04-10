package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

import java.time.LocalDateTime

/**
 * Represents one event (like sunrise or sunset). Contains the exact time of the event happening, and
 * a variable which says whether the conditions will be good for photographs of the event.
 */
data class SunEvent (
    val time: LocalDateTime,
    val conditions: WeatherConditionsRating
)