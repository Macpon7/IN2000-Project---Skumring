package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.ForecastDao
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.ForecastEntity
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.ImageDao
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.ImageEntity
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoDao
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoEntity
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.forecast.ForecastRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.forecast.ForecastRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.forecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.InternetException
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.ImageDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.SunEvent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalDateTime

private const val TAG = "PlaceRepository"

interface PlaceRepository {
    suspend fun getAllPlaces(): List<PlaceInfo>
    suspend fun getPlace(placeId: Int): PlaceInfo
    suspend fun getUserLocationPlace(lat: String, long: String): PlaceInfo
    suspend fun getFavourites(): List<PlaceInfo>
    suspend fun getCustomPlaces(): List<PlaceInfo>
    suspend fun addCustomPlace(place: PlaceInfo, imageUri: Uri, imageTimestamp: LocalDate)
    suspend fun removeCustomPlace(placeId: Int)
    suspend fun makeFavourite(placeId: Int)
    suspend fun unmakeFavourite(placeId: Int)
    suspend fun getImages(placeId: Int): List<ImageDetails>
    suspend fun saveImageToInternalStorage(contentUri: Uri, placeId: Int, timestamp: LocalDate)

}
class PlaceRepositoryImpl(
    //Creates and initializes the database
    //var database : AppDatabase = AppDatabase.getDatabase(context = LocalContext.current),
    private val placeInfoDao: PlaceInfoDao,
    private val forecastDao: ForecastDao,
    private val imageDao: ImageDao,
    private val locationForecastDataSource: LocationForecastDataSource = LocationForecastDataSource(),
    private val forecastRepository:ForecastRepository = ForecastRepositoryImpl(),
    private val context: Context
): PlaceRepository {


    override suspend fun saveImageToInternalStorage(contentUri: Uri, placeId: Int, timestamp: LocalDate) {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        try {
            val contentResolver: ContentResolver = context.contentResolver
            inputStream = contentResolver.openInputStream(contentUri)
            val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

            val fileDir = File(context.filesDir, "/placeImages/")
            if (!fileDir.exists()) {
                fileDir.mkdir()
            }

            val fileName = "$placeId.jpg"
            val file = File(fileDir, fileName)

            outputStream = withContext(Dispatchers.IO) {
                FileOutputStream(file)
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            // Insert the image details into the database
            imageDao.insertImage(ImageEntity(
                placeId = placeId,
                imgPath = "/placeImages/$fileName",
                timestamp = timestamp))

            Log.d("MyPage", "added to database")
            Log.d("MyPage", "the new path is /placeImages/$fileName")
        } catch (e: IOException) {
            Log.e(TAG, "Error saving image", e)
            e.printStackTrace()
        } finally {
            inputStream?.withContext(Dispatchers.IO) {
                close()
            }
            outputStream?.withContext(Dispatchers.IO) {
                close()
            }
        }
    }

    //bruke ImageDetails() under model


    override suspend fun getAllPlaces(): List<PlaceInfo> {
        //TODO("Not yet implemented")
        // 1. Get all places from DB (missing SunEvents)
        // 2. For each place, check the forecast info (in separate function)
        // 3. Send back the list of PlaceInfo objects

        // The first result from DB is without forecast information
        var allPlaces = getAllPlacesFromDb()

        // Here we fetch the forecast info for each place
        runBlocking {
            allPlaces = allPlaces.map {
                async {
                    it.copy(sunEvents = getForecastData(
                        placeId = it.id,
                        lat = it.lat,
                        long = it.long)
                    )
                }
            }.awaitAll()
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
            val sunEventsList = try {
                fetchNewForecastData(lat = lat, long = long)
            } catch (e: Exception) {
                throw Exception("Error fetching new forecast data", e)
            }

            // Save sun event list to DB with correct placeId
            upsertForecastData(sunEvents = sunEventsList, placeId = placeId)

            return sunEventsList

        } else if (dataFromDb[0].timestamp.isBefore(LocalDateTime.now().minusHours(1))) {
            // If there is data, but it is older than one hour, also fetch new data

            // Fetch data from API
            val sunEvents = try {
                fetchNewForecastData(lat = lat, long = long)
            } catch (e: Exception) {
                throw Exception("Error fetching new forecast data", e)
            }

            // Update Forecast objects in the DB
            upsertForecastData(
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
                    ),
                    goldenHourTime = it.goldenHourDateTime,
                    blueHourTime = it.blueHourDateTime
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
        val fullForecast = try {
            locationForecastDataSource.fetchWeatherData(lat = lat, long = long)
        } catch (e: Exception) {
            throw InternetException("Could not fetch forecast data from MET", e)
        }

        // Group all the forecast data by date
        var forecastGroupedByDate = fullForecast.groupBy { it.time.toLocalDate() }.toSortedMap()

        // Remove all data that is long term forecast. This means that we only keep the data for today, tomorrow, and the day after tomorrow
        forecastGroupedByDate = forecastGroupedByDate.headMap(forecastGroupedByDate.keys.elementAt(3))

        // Send our map to a function which will return a list of SunEvent objects
        return try {
            forecastRepository.makeSunEvents(
                forecastGroupedByDate = forecastGroupedByDate,
                lat = lat,
                long = long
            )
        } catch (e: Exception) {
            throw InternetException("Could not fetch sunrise data from MET", e)
        }
    }

    private fun upsertForecastData(
        oldForecastData: List<ForecastEntity> = emptyList(),
        sunEvents: List<SunEvent>,
        placeId: Int) {
        // Convert list of SunEvent objects into list of ForecastEntity objects
        val currentTimeStamp = LocalDateTime.now()
        val forecasts = sunEvents.map {
            // Get the right id to use by getting the id of the item at the same index
            // in oldForecastData
            val oldId = if (oldForecastData.isNotEmpty()) {
                oldForecastData[sunEvents.indexOf(it)].id
            } else {
                0
            }
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
                goldenHourDateTime = it.goldenHourTime,
                blueHourDateTime = it.blueHourTime,
                timestamp = currentTimeStamp
            )
        }


        // Insert the ForecastEntity objects into the database
        forecastDao.upsertForecasts(forecasts)
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
                images = getImages(placeId = it.id),
                sunEvents = emptyList()
            )
        }
    }

    /**
     * Gets a PlaceInfo object from our database, given an id
     */
    override suspend fun getPlace(placeId: Int): PlaceInfo {
        Log.d(TAG, "Trying to load place with id: $placeId from DB")

        val placeEntity: PlaceInfoEntity = placeInfoDao.getOnePlace(placeId = placeId)

        //TODO fetch images
        return PlaceInfo(
            id = placeEntity.id,
            name = placeEntity.name,
            description = placeEntity.description,
            lat = placeEntity.latitude,
            long = placeEntity.longitude,
            isFavourite = placeEntity.isFavourite,
            isCustomPlace = placeEntity.isCustomPlace,
            hasNotification = placeEntity.hasNotification,
            images = getImages(placeId = placeEntity.id),
            sunEvents = getForecastData(
                placeId = placeEntity.id,
                lat = placeEntity.latitude,
                long = placeEntity.longitude)
        )
    }

    /**
     *
     */
    override suspend fun getUserLocationPlace(lat: String, long: String): PlaceInfo {
        Log.d(TAG, "Trying to create PlaceInfo object at user's current location")

        // This will never have any images, so images here is just an empty list
        return PlaceInfo(
            id = 0,
            name = "",
            description = "",
            lat = lat,
            long = long,
            isFavourite = false,
            isCustomPlace = false,
            hasNotification = false,
            images = emptyList(),
            sunEvents = fetchNewForecastData(
                lat = lat,
                long = long)
        )
    }

    /**
     * TODO
     */
    override suspend fun getFavourites(): List<PlaceInfo> {
        val placesFromDb = placeInfoDao.getFavourites()

        // Id there are no favourites, return an empty list
        if (placesFromDb.isEmpty()) {
            return emptyList()
        }

        try {
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
                    images = getImages(it.id),
                    sunEvents = getForecastData(
                        placeId = it.id,
                        lat = it.latitude,
                        long = it.longitude
                    )
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getCustomPlaces(): List<PlaceInfo> {
        val entities = placeInfoDao.getCustomPlaces()
        if (entities.isEmpty()) {
            return emptyList()
        }

        try {
            return entities.map {
                PlaceInfo(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    lat = it.latitude,
                    long = it.longitude,
                    isFavourite = it.isFavourite,
                    isCustomPlace = it.isCustomPlace,
                    hasNotification = it.hasNotification,
                    images = getImages(it.id),
                    sunEvents = getForecastData(
                        placeId = it.id,
                        lat = it.latitude,
                        long = it.longitude
                    )
                )
            }
        } catch (e: Exception) {
            throw e
        }
    }


    /**
     * Inserts a new place in the database, and returns the placeId value assigned to this place
     */
    override suspend fun addCustomPlace(
        place: PlaceInfo,
        imageUri: Uri,
        imageTimestamp: LocalDate
        ) {

        // Check that we have an internet connection by making a request to locationforecast
        try {
            val testForecast = locationForecastDataSource.fetchWeatherData(lat = place.lat, long = place.long)
        } catch (e: Exception) {
            throw InternetException("Could not get weather data when adding new place to DB", e)
        }


        //input is PlaceInfo object
        val placeInfoEntity = PlaceInfoEntity(
            name = place.name,
            description = place.description,
            latitude = place.lat,
            longitude = place.long,
            isCustomPlace = true,
            isFavourite = false,
            hasNotification = false
        )

        placeInfoDao.insertCustomPlace(placeInfoEntity)

        // Calling this will fetch weather data for the newly added custom place, and let us get its ID
        //The newest custom place will always be the last in the list, here we fetch its ID and returns to the caller
        val newId = getCustomPlaces().last().id

        saveImageToInternalStorage(contentUri = imageUri, placeId = newId, timestamp = imageTimestamp)
    }

    /**
     *This methods removes a tuple from the table based on the placeId
     */
    override suspend fun removeCustomPlace(placeId: Int) {
        val customPlace: Boolean = placeInfoDao.checkIfCustomPlace(placeId)
        if (customPlace){
            // Delete all forecasts associated with this place
            forecastDao.deleteForecasts(placeId = placeId)

            // Delete images associated with this place
            imageDao.deleteImages(placeId = placeId)

            // Finally, delete the place itself
            placeInfoDao.deleteCustomPlace(placeId = placeId)
        } else {
            throw IllegalArgumentException("Cannot delete a non-custom place")
        }
    }

    /**
    *This methods sets a location as favorite by setting is_custom_place = 1
     *
     */
    override suspend fun makeFavourite(placeId: Int) {
        placeInfoDao.markAsFavorite(placeId)
    }

    /**
     * This methods sets a location as favorite by setting is_custom_place = 0
     */
    override suspend fun unmakeFavourite(placeId: Int) {
        placeInfoDao.unmarkAsFavorite(placeId)
    }

    /**
     * TODO
     */
    override suspend fun getImages(placeId: Int): List<ImageDetails> {
        val entities = imageDao.getImages(placeId = placeId)

        return if (entities.isEmpty()) {
            emptyList()
        } else {
            return entities.map {
                ImageDetails(
                    path = it.imgPath,
                    timeStamp = it.timestamp
                )
            }
        }
    }
}


