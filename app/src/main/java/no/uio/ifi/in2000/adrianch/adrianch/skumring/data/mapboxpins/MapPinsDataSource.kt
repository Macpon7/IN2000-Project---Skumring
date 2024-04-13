package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins

import android.util.Log
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo

class MapPinsDataSource {

    // Constant for logging errors:
    private val logTag : String = "MapPinsDataSource"

    suspend fun fetchMapPins(): List<PinInfo> {
        return presetPinsInfo
    }
}