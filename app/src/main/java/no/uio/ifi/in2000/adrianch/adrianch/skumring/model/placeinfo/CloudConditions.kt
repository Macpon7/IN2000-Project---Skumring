package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

enum class CloudConditions (val weightLow: Int, val weightMedium: Int, val weightHigh: Int) {
    CLOUDY(weightLow = 9, weightMedium = 4, weightHigh = 2),
    FAIR(weightLow = 9, weightMedium = 4, weightHigh = 2),
    CLEAR(weightLow = 9, weightMedium = 4, weightHigh = 2)
}