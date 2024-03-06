package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecastapi

data class Properties(
    val meta: Meta,
    val timeseries: List<Timeseries>
)