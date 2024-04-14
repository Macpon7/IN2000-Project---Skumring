package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceDetailsDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource

interface PlaceInfoRepository {
    suspend fun getAllPlaces()
    suspend fun getPlace(id: Int)
    suspend fun getFavourites()
    suspend fun getCustomPlaces()
    suspend fun insertCustomPlace(place: PlaceInfoEntity)
    suspend fun removeCustomPlace(id: Int)
    suspend fun makeFavourite(id: Int)
    suspend fun unmakeFavourite(id: Int)
}
class PlaceInfoRepositoryImpl(
    //Creates and initializes the database
    //var database : AppDatabase = AppDatabase.getDatabase(context = LocalContext.current),
    private val placeInfoDao: PlaceInfoDao,
    private val forecastDao: ForecastDao,
    private val imageDao: ImageDao,
    private val locationForecastDataSource: LocationForecastDataSource = LocationForecastDataSource(),
    private val sunriseDataSource: SunriseDataSource = SunriseDataSource(),
    private val placeDetailsDataSource: PlaceDetailsDataSource = PlaceDetailsDataSource(),
    private val placeListRepository: PlaceListRepository = PlaceListRepositoryImpl()
): PlaceInfoRepository {
    private val oldPlaceInfoRepository = OldPlaceInfoRepositoryImpl(
        sunriseDataSource = sunriseDataSource,
        locationForecastDataSource = locationForecastDataSource,
        placeDetailsDataSource = placeDetailsDataSource
    )

    init {
        runBlocking {
            launch (Dispatchers.IO) {
                val places = placeInfoDao.getAllPlaces()
            }
        }
    }

    override suspend fun getAllPlaces() {
        TODO("Not yet implemented")
    }

    override suspend fun getPlace(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getFavourites() {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomPlaces() {
        TODO("Not yet implemented")
    }

    override suspend fun insertCustomPlace(place: PlaceInfoEntity){
        //appDatabase.placeInfoDao().insert(place)
    }

    override suspend fun removeCustomPlace(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun makeFavourite(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun unmakeFavourite(id: Int) {
        TODO("Not yet implemented")
    }
}
