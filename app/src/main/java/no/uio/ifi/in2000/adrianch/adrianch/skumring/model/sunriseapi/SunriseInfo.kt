package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.sunriseapi

data class SunriseInfo(val licenseURL: String = "",
                       val copyright: String = "",
                       val geometry: Geometry,
                       val type: String = "",
                       val `when`: When,
                       val properties: Properties)