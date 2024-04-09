package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

class DatabaseRepository(private val appDatabase: AppDatabase) {

    suspend fun insertPlace(place: PlaceInfoEntity){
        appDatabase.placeInfoDao().insert(place)
    }

    fun getAllPlaces() = appDatabase.placeInfoDao().getAllPlaces()
}