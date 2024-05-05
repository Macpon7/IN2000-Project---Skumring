package no.uio.ifi.in2000.adrianch.adrianch.skumring.model.settings

import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings.Language
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings.Location
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings.Theme

data class Settings (
    val theme: Theme,
    val language: Language,
    val location: Location
)