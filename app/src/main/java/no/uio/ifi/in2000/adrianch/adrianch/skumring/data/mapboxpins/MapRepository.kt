package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins

import android.util.Log
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo


interface MapRepository {
    suspend fun getPins(): List<PinInfo>
}

    private const val logTag = "MapRepo" //for logging


class MapRepositoryImpl(
    private val mapPinsDataSource: MapPinsDataSource = MapPinsDataSource()
) : MapRepository {

    override suspend fun getPins(): List<PinInfo> {
        try {
            return mapPinsDataSource.fetchMapPins()
        } catch (e: Exception) {
            Log.e(logTag, e.message, e)
            throw e
        }
    }
}
