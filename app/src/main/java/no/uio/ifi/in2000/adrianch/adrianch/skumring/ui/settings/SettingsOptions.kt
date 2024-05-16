package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

import no.uio.ifi.in2000.adrianch.adrianch.skumring.R

// Enum classes with settings
enum class Theme(val stringResourceId: Int) {
    FOLLOW_SYSTEM(R.string.follow_system),
    DARK_MODE(R.string.dark_mode),
    LIGHT_MODE(R.string.light_mode)
}

enum class Language(val stringResourceId: Int) {
    FOLLOW_SYSTEM(R.string.follow_system),
    NORWEGIAN(R.string.norwegian),
    ENGLISH(R.string.english)
}

enum class Location(val stringResourceId: Int) {
    COSTUM_LOCATION(R.string.costum_location),
    PHONES_LOCATION(R.string.phones_location)
}