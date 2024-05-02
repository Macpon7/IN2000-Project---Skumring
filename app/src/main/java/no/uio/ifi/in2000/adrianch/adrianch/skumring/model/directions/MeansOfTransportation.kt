package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions

import no.uio.ifi.in2000.adrianch.adrianch.skumring.R

enum class MeansOfTransportation(val stringResourceId: Int, val apiCall: String) {
    WALKING(stringResourceId = R.string.means_of_transport_walking, apiCall = "walking"),
    BIKING(stringResourceId = R.string.means_of_transportation_biking, apiCall = "cycling"),
    DRIVING(stringResourceId = R.string.means_of_transportation_driving, apiCall = "driving")
}