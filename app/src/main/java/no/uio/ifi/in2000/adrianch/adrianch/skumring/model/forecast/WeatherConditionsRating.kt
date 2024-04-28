package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast

import no.uio.ifi.in2000.adrianch.adrianch.skumring.R

/**
 * Rating between 1-3 stars depending on conditions
 */
enum class WeatherConditionsRating(val stringResourceId: Int) {
    POOR(stringResourceId = R.string.conditions_poor),
    DECENT(stringResourceId = R.string.conditions_decent),
    EXCELLENT(stringResourceId = R.string.conditions_excellent)
}