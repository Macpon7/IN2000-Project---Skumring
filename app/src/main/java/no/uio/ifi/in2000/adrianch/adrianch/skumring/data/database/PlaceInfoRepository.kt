package no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.locationforecast.LocationForecastDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceDetailsDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.sunrise.SunriseDataSource
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour

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



            //Following code is to populate the database
            /*
                val allPlaces = placeListRepository.getPresetPlaceList()
                allPlaces.forEach{
                    placeInfoDao.insert(
                        PlaceInfoEntity(
                            name = it.name,
                            description = it.description,
                            latitude = it.lat,
                            longitude = it.long,
                            isCustomPlace = 0,
                            isFavourite = 0
                        )
                    )
                }

             */


            }
        }
    }

    override suspend fun getAllPlaces() {
        TODO("Not yet implemented")
    }


    override suspend fun getPlace(id: Int) {
        TODO("Not yet implemented")
        //placeInfoDao.getPlace(id)
    }



    override suspend fun getFavourites() {
        placeInfoDao.getFavourites()
    }

    override suspend fun getCustomPlaces() {
        TODO("Not yet implemented")
    }


//works but should take another input
    override suspend fun insertCustomPlace(place: PlaceInfoEntity){
        //input is PlaceInfo object
        //API is called so user can access weather forecast immidieately
        placeInfoDao.insertCustomPlace(place)
    }

    /**
     *This methods removes a tuple from the table based on the placeId
     */
    override suspend fun removeCustomPlace(placeId: Int) {
        val customPlace: Int? = placeInfoDao.checkIfCustomPlace(placeId)
        if (customPlace == 1){
            placeInfoDao.deleteCustomPlace(placeId)
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
     *This methods sets a location as favorite by setting is_custom_place = 0
     */
    override suspend fun unmakeFavourite(placeId: Int) {
        placeInfoDao.unmarkAsFavorite(placeId)
    }

    //Image features

    suspend fun insertDefaultImage(path: String){
        //painterResource(R.drawable.solnedgang)
       // var imageEntity: ImageEntity = ImageEntity(placeId = 1, )
        //imageDao.insert()
    }
}


