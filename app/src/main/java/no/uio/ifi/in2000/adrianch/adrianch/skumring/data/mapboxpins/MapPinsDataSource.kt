package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins

import android.util.Log
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo

class MapPinsDataSource {

    // Constant for logging errors:
    private val logTag : String = ""

    suspend fun fetchMapPins(): List<PinInfo> {
        try {
            return presetPinsInfo
        } catch (e : Exception) {
            Log.e(logTag, e.message, e)
            throw e
        }
    }
}