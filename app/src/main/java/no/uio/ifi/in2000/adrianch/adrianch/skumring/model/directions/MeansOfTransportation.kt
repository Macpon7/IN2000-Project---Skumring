package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions

import no.uio.ifi.in2000.adrianch.adrianch.skumring.R

enum class MeansOfTransportation(val stringResourceId: Int) {
    WALKING(stringResourceId = R.string.means_of_transport_walking),
    BIKING(stringResourceId = R.string.means_of_transportation_biking),
    DRIVING(stringResourceId = R.string.means_of_transportation_driving)
}