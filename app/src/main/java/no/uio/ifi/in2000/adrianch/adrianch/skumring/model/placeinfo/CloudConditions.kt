package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

/**
 * Cloudy is when cloudAreaFraction > 67.0
 * Fair is when < 33.0 cloudAreaFraction < 67.0
 * Clear is when cloudAreaFraction < 33.0
 */
enum class CloudConditions {
    CLOUDY,
    FAIR,
    CLEAR
}