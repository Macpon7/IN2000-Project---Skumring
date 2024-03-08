package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi

data class Properties(val solarnoon: Solarnoon,
                      val sunrise: Sunrise,
                      val sunset: Sunset,
                      val body: String = "",
                      val solarmidnight: Solarmidnight)