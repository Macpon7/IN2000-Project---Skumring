package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins

import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo

interface MapRepository {
    suspend fun getPins(): List<PinInfo>
}

class MapRepositoryImpl(
    private val mapPinsDataSource: MapPinsDataSource = MapPinsDataSource()

) : MapRepository {

    override suspend fun getPins(): List<PinInfo> {

        return mapPinsDataSource.fetchMapPins()
    }
}