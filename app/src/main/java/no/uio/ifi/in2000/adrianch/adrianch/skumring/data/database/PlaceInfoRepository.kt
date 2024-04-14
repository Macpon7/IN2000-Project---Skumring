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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditions
import java.time.LocalDateTime

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
        //TODO("Not yet implemented")
        // 1. Get all places from DB (missing SunEvents)
        // 2. For each place, check the forecast info (in separate function)
        // 3. Send back the list of PlaceInfo objects

        val placesWithoutForecast = getAllPlacesFromDb()

        //awaitAll()
    }

    /**
     * Gets the Forecast data for a given placeId from and returns it as a list of SunEvent Objects. First
     * we try to get forecast from the DB. If there is no forecast information for this place, or
     * the information is older than 1 hour, we call MET's LocationForecast and Sunrise APIs and compute new SunEvent
     * objects based on this data. Then the new forecast data is stored in the DB, overwriting any
     * old data for this placeId.
     */
    private suspend fun getForecastData(placeId: Int): List<SunEvent> {
        // Try to fetch from DB
        val dataFromDb = forecastDao.getForecasts(placeId = placeId)

        if (dataFromDb.isEmpty()) {
            // If there is no data in the DB for this placeId, we need to fetch data from APIs
            TODO()
            // Fetch data from API, using functions in oldPlaceInfoRepo


        } else if (dataFromDb[0].timestamp.isBefore(LocalDateTime.now().minusHours(1))) {
            // If there is data, but it is older than one hour, also fetch new data
            TODO()


        } else {
            // If there is good data AND it is less than 1 hour old, compute the list and return it
            return dataFromDb.asList().map {
                SunEvent(
                    time = it.sunsetDateTime,
                    tempAtEvent = it.sunsetTemp,
                    weatherIcon = it.weatherIcon,
                    conditions = WeatherConditions(
                        weatherRating = it.weatherRating,
                        cloudConditionLow = it.cloudConditionLow,
                        cloudConditionMedium = it.cloudConditionMedium,
                        cloudConditionHigh = it.cloudConditionHigh,
                        airCondition = it.airCondition
                    )
                )
            }
        }
    }

    /**
     * Gets all the locations in the DB, converts their information to [PlaceInfo] objects
     * and returns them as a List<[PlaceInfo]>. This function purposefully does not convert the
     * forecast data from the DB to SunEvent, that is handled in getAllPlaces() which uses this
     * private function.
     */
    private suspend fun getAllPlacesFromDb(): List<PlaceInfo> {
        val placesFromDb = placeInfoDao.getAllPlaces()

        return placesFromDb.map {
            // TODO get images from DB and convert to List<ImageDetails>
            PlaceInfo(
                id = it.id,
                name = it.name,
                description = it.description,
                lat = it.latitude,
                long = it.longitude,
                isFavourite = it.isFavourite,
                isCustomPlace = it.isCustomPlace,
                hasNotification = false,
                images = emptyList(),
                sunEvents = emptyList()
            )
        }
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
