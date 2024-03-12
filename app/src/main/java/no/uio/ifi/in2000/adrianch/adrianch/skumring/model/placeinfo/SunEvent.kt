package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo

import java.time.LocalDateTime

data class SunEvent (
    val time: LocalDateTime,
    val conditions: Boolean
)