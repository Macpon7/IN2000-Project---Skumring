package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi

data class Geometry(val coordinates: List<Int>,
                    val type: String = "")

data class Properties(val solarnoon: Solarnoon,
                      val sunrise: Sunrise,
                      val sunset: Sunset,
                      val body: String = "",
                      val solarmidnight: Solarmidnight)

data class Solarmidnight(val visible: Boolean = false,
                         val time: String,
                         val discCentreElevation: Double = 0.0)

data class Solarnoon(val visible: Boolean = false,
                     val time: String,
                     val discCentreElevation: Double = 0.0)

data class Sunrise(val azimuth: Double = 0.0,
                   val time: String)

data class SunriseInfo(val licenseURL: String = "",
                       val copyright: String = "",
                       val geometry: Geometry,
                       val type: String = "",
                       val `when`: When,
                       val properties: Properties)

data class Sunset(val azimuth: Double = 0.0,
                  val time: String,
)

data class When(val interval: List<String>?)



