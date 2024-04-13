package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.currentCoroutineContext

interface DatabaseRepository {
    suspend fun insertPlace(place: PlaceInfoEntity)
}
class DatabaseRepositoryImpl(
    //Creates and initializes the database
    //var database : AppDatabase = AppDatabase.getDatabase(context = LocalContext.current),
    var dbDAO: PlaceInfoDao
): DatabaseRepository {

    override suspend fun insertPlace(place: PlaceInfoEntity){
        //appDatabase.placeInfoDao().insert(place)
    }

    //fun getAllPlaces() = appDatabase.placeInfoDao().getAllPlaces().asLiveData()

}
