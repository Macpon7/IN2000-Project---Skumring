package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo

class MapPinsDataSource {
    suspend fun fetchMapPins(): List<PinInfo> {
        return presetPinsInfo
    }
}