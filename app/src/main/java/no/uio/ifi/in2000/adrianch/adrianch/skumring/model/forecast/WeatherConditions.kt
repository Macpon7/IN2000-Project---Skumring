package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast

data class WeatherConditions (
    val weatherRating: WeatherConditionsRating,
    val cloudConditionLow: CloudConditions,
    val cloudConditionMedium: CloudConditions,
    val cloudConditionHigh: CloudConditions,
    val airCondition: AirConditions
)