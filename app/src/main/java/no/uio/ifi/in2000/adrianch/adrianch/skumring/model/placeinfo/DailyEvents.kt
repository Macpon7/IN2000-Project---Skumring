package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

/**
 * An instance of DailyEvents represents one calendar day. It can contain multiple SunEvent objects,
 * one for each event we are interested in. For our MVP we only want sunset, but in later versions we
 * might add sunrise, or even moon related events.
 */
data class DailyEvents (
    val sunset: SunEvent,
)