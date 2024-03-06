package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi

data class Next6HoursDetails(
    val air_temperature_max: Double,
    val air_temperature_min: Double,
    val precipitation_amount: Double,
    val precipitation_amount_max: Double,
    val precipitation_amount_min: Double,
    val probability_of_precipitation: Double
)