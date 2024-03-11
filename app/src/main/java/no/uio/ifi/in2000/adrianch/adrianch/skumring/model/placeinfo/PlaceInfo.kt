package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

data class PlaceInfo (
    val name: String,
    val description: String,
    val latitude: String,
    val longitude: String,
    val sunEvents: List<DailyEvents>
)