package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions

enum class MeansOfTransportation(val apiCall: String) {
    WALKING(apiCall = "walking"),
    BIKING(apiCall = "cycling"),
    DRIVING(apiCall = "driving")
}