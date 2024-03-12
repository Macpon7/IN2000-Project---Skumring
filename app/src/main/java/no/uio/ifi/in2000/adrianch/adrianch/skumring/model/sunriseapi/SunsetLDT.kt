package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SunsetLDT (
    val azimuth: Double = 0.0,
    val time: String  //returnerer: 2024-03-07T17:03+00:00
){
    val datetime: LocalDateTime = convertStringToLocalDateTime(time)

    private fun convertStringToLocalDateTime(time: String): LocalDateTime{
        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        return LocalDateTime.parse(time, formatter)
    }
}
