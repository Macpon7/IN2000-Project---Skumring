package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector

// Enum classes with settings

// Lag det som alpaca

enum class Theme() {
    FOLLOW_SYSTEM,
    DARK_MODE,
    LIGHT_MODE
}

enum class Language {
    FOLLOW_SYSTEM,
    NORWEGIAN,
    ENGLISH
}

enum class Location {
    COSTUM_LOCATION,
    PHONES_LOCATION
}