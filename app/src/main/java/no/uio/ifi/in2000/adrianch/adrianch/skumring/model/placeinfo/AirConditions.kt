package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

/**
 * High is when relative_humidity > 67.0
 * Mid is when < 33.0 relative_humidity < 67.0
 * Low is when relative_humidity < 33.0
 */
enum class AirConditions {
    LOW,
    MID,
    HIGH
}