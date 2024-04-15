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
    suspend fun getAllPlaces(): List<PlaceInfo>
    suspend fun getPlace(id: Int): PlaceInfo
    suspend fun getFavourites(): List<PlaceInfo>
    suspend fun getCustomPlaces(): List<PlaceInfo>
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

    override suspend fun getAllPlaces(): List<PlaceInfo> {
        //TODO("Not yet implemented")
        // 1. Get all places from DB (missing SunEvents)
        // 2. For each place, check the forecast info (in separate function)
        // 3. Send back the list of PlaceInfo objects

        // The first result from DB is without forecast information
        val allPlaces = getAllPlacesFromDb()

        // Here we fetch the forecast info for each place
        allPlaces.forEach {
            it.sunEvents = getForecastData(it.id, it.lat, it.long)
        }

        return allPlaces
        //awaitAll()
    }

    /**
     * Gets the Forecast data for a given placeId from and returns it as a list of SunEvent Objects. First
     * we try to get forecast from the DB. If there is no forecast information for this place, or
     * the information is older than 1 hour, we call MET's LocationForecast and Sunrise APIs and compute new SunEvent
     * objects based on this data. Then the new forecast data is stored in the DB, overwriting any
     * old data for this placeId.
     */
    private suspend fun getForecastData(placeId: Int, lat: String, long: String): List<SunEvent> {
        // Try to fetch from DB
        val dataFromDb = forecastDao.getForecasts(placeId = placeId)

        if (dataFromDb.isEmpty()) {
            // If there is no data in the DB for this placeId, we need to fetch data from APIs

            // Fetch data from API, using code taken from oldPlaceInfoRepo
            val sunEventsList = fetchNewForecastData(lat = lat, long = long)

            // Save sun event list to DB with correct placeId
            insertNewForecastData(sunEvents = sunEventsList, placeId = placeId)

            return sunEventsList

        } else if (dataFromDb[0].timestamp.isBefore(LocalDateTime.now().minusHours(1))) {
            // If there is data, but it is older than one hour, also fetch new data

            // Fetch data from API
            val sunEvents = fetchNewForecastData(lat = lat, long = long)

            // Update Forecast objects in the DB
            updateForecastData(
                oldForecastData = dataFromDb,
                sunEvents = sunEvents,
                placeId = placeId)

            return sunEvents

        } else {
            // If there is good data AND it is less than 1 hour old, compute the list and return it
            return dataFromDb.map {
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
     * Fetches new forecast data for the given coordinates from the API and returns it
     * as a list of [SunEvent] objects. One object for each day of the forecast.
     */
    private suspend fun fetchNewForecastData (lat: String, long: String): List<SunEvent> {
        // Get the forecasted weather at this place
        val fullForecast = locationForecastDataSource.fetchWeatherData(lat = lat, long = long)

        // Group all the forecast data by date
        val forecastGroupedByDate = fullForecast.groupBy { it.time.toLocalDate() }

        // Send our map to a function which will return a list of SunEvent objects
        return oldPlaceInfoRepository.makeSunEvents(
            forecastGroupedByDate = forecastGroupedByDate,
            lat = lat,
            long = long
        )
    }

    /**
     * Inserts a list of SunEvent objects for the given placeId in the DB
     */
    private suspend fun insertNewForecastData(sunEvents: List<SunEvent>, placeId: Int) {
        // Convert list of SunEvent objects into list of ForecastEntity objects
        val currentTimeStamp = LocalDateTime.now()
        val forecasts = sunEvents.map {
            ForecastEntity(
                placeId = placeId,
                sunsetDateTime = it.time,
                weatherRating = it.conditions.weatherRating,
                cloudConditionLow = it.conditions.cloudConditionLow,
                cloudConditionMedium = it.conditions.cloudConditionMedium,
                cloudConditionHigh = it.conditions.cloudConditionHigh,
                airCondition = it.conditions.airCondition,
                sunsetTemp = it.tempAtEvent,
                weatherIcon = it.weatherIcon,
                timestamp = currentTimeStamp
            )
        }

        // Insert the ForecastEntity objects into the database
        forecastDao.insertForecasts(forecasts)
    }

    private suspend fun updateForecastData(
        oldForecastData: List<ForecastEntity>,
        sunEvents: List<SunEvent>,
        placeId: Int) {
        // Convert list of SunEvent objects into list of ForecastEntity objects
        val currentTimeStamp = LocalDateTime.now()
        val forecasts = sunEvents.map {
            // Get the right id to use by getting the id of the item at the same index
            // in oldForecastData
            val oldId = oldForecastData[sunEvents.indexOf(it)].id
            ForecastEntity(
                id = oldId,
                placeId = placeId,
                sunsetDateTime = it.time,
                weatherRating = it.conditions.weatherRating,
                cloudConditionLow = it.conditions.cloudConditionLow,
                cloudConditionMedium = it.conditions.cloudConditionMedium,
                cloudConditionHigh = it.conditions.cloudConditionHigh,
                airCondition = it.conditions.airCondition,
                sunsetTemp = it.tempAtEvent,
                weatherIcon = it.weatherIcon,
                timestamp = currentTimeStamp
            )
        }


        // Insert the ForecastEntity objects into the database
        forecastDao.updateForecasts(forecasts)
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

    override suspend fun getPlace(id: Int): PlaceInfo {
        TODO("Not yet implemented")
    }

    override suspend fun getFavourites(): List<PlaceInfo> {
        TODO("Not yet implemented")
    }

    override suspend fun getCustomPlaces(): List<PlaceInfo> {
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
