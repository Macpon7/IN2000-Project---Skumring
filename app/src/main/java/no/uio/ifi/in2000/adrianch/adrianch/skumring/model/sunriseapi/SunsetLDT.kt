package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class SunsetLDT (
    val azimuth: Double = 0.0,
    val time: String  //returnerer: 2024-03-07T17:03+00:00
){

    //Ting jeg har prøvd:

    //val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX") //Value(YearOfEra,4,19,EXCEEDS_PAD)'-'Value(MonthOfYear,2)'-'Value(DayOfMonth,2)'T'Value(HourOfDay,2)':'Value(MinuteOfHour,2)Offset(+HHmm,'Z')
    //val offsetDateTime = OffsetDateTime.parse(time, formatter)
    //val datetime: LocalDateTime = LocalDateTime.parse(time)
    //convertStringToLocalDateTime(time)
    private fun convertStringToLocalDateTime(time: String): LocalDateTime{
        return LocalDateTime.parse(time)
    }

    private fun converter(time: String){
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
    }

}
/*
fun convertStringToLocalDateTime(time: String): LocalDateTime{
    return LocalDateTime.parse(time)
}

 companion object {
        fun convertStringToLocalDateTime(time: String): LocalDateTime {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
            return LocalDateTime.parse(time, formatter)
        }
    }
 */