package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins

import android.util.Log
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo

class MapPinsDataSource {

    // Constant for logging errors:
    private val logTag : String = ""

    suspend fun fetchMapPins(): List<PinInfo> {
        try {
            return presetPinsInfo
        } catch (e : NoSuchElementException) {
            Log.e(logTag, "Element not found: ${e.message} in fetchMapPins" , e)
            throw e
        } catch (e : IndexOutOfBoundsException) {
            Log.e(logTag, "Index out of bounds: ${e.message} in fetchMapPins" , e)
            throw e
        } catch (e : Exception) {
            Log.e(logTag, "An unexpected error: ${e.message} in fetchMapPins" , e)
            throw e
        }
    }
}