package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi

data class Next1HoursDetails(
    val precipitation_amount: Double,
    val precipitation_amount_max: Double,
    val precipitation_amount_min: Double,
    val probability_of_precipitation: Double,
    val probability_of_thunder: Double
)