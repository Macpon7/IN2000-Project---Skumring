package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ApplicationSkumring
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding.GeocodingRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding.GeocodingRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings.Theme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HomeUiState(
    // Setting up dummy info, default location to OJD
    val date: LocalDate = LocalDate.of(2000, 1, 1),
    var long: String = "0",
    var lat: String = "0",
    val temp: String = "N/A",
    val sunsetTime: String = "N/A",
    val sunsetDate: String = "N/A",
    val sunsetWeatherIcon: String? = null,
    val weatherConditions: WeatherConditionsRating = WeatherConditionsRating.POOR,
    val blueHour: String = "N/A",
    val goldenHour: String = "N/A",
    val placeName: String = "",

    // Variable to check if the userlocation is ready
    var userLocationReady: Boolean = false,

    var favoritePlaces: List<PlaceInfo> = emptyList(),

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

private const val logTag = "HomeViewModel" //for logging

/**
 * ViewModel for HomeScreen
 */
@SuppressLint("StaticFieldLeak")
class HomeViewModel(
    private val placeRepository: PlaceRepository,
    private val context: Context
) : ViewModel() {

    private val userLocationRepository: UserLocationRepository =
        UserLocationRepositoryImpl(context = context)
    private var userPlace: PlaceInfo = PlaceInfo(
        id = 0,
        name = "",
        description = "",
        lat = "",
        long = "",
        isFavourite = false,
        isCustomPlace = false,
        hasNotification = false,
        images = emptyList(),
        sunEvents = emptyList()
    )
    private val geocodingRepository: GeocodingRepository = GeocodingRepositoryImpl()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private val blueHourIconLightMode = R.drawable.bluesunlightmode
    private val blueHourIconDarkMode = R.drawable.bluesundarkmode

    init {
        //loadHomeScreen()
    }

    fun loadHomeScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            loadUserLocation()
            loadFavourites()
            updateWeather()
        }
    }

    private fun loadFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeUiState.update { currentHomeUiState ->
                val favourites = placeRepository.getFavourites()

                currentHomeUiState.copy(favoritePlaces = favourites)
            }
        }
    }

    // TODO Should we just throw this into updateWeather?
    /**
     *    Updates coordinates used to ask for weather to devices' current coords
     */
    @SuppressLint("StringFormatInvalid")
    private fun loadUserLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _homeUiState.update { currentHomeUiState ->
                    val userLoc = userLocationRepository.getUserLocation()
                    val userLat = userLoc.lat
                    val userLong = userLoc.long
                    val userPlaceName = geocodingRepository.getPlaceNameFromCoordinates(
                        lat = userLat,
                        long = userLong
                    )
                    Log.d(logTag + "LoadUserLoc", "Lat: ${userLoc.lat}, Long: ${userLoc.long}")
                    currentHomeUiState.copy(
                        lat = userLat,
                        long = userLong,
                        placeName = userPlaceName.placeName
                    )
                }
            } catch (e: Exception) {
                // Practically no way this should happen
                Log.e(logTag, "Error updating user location", e)
                _homeUiState.update { currentHomeUiState ->
                    currentHomeUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_loadUserLocation)
                    )
                }
            }
        }
    }

    private fun updateWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(2000) // make it wait again until it tries again

                // check if the lat and long is updated
                if (homeUiState.value.lat != "0" && homeUiState.value.long != "0") {
                    userPlace = placeRepository.getUserLocationPlace(
                        lat = homeUiState.value.lat,
                        long = homeUiState.value.long
                    )

                    _homeUiState.update { currentHomeUiState ->

                        // will show location when the userlocation is available
                        currentHomeUiState.copy(
                            temp = userPlace.sunEvents[0].tempAtEvent,
                            sunsetTime = userPlace.sunEvents[0].time.toLocalTime().format(
                                DateTimeFormatter.ofPattern("HH':'mm")
                            ),
                            sunsetDate = userPlace.sunEvents[0].time.toLocalDate().format(
                                DateTimeFormatter.ISO_LOCAL_DATE
                            ),
                            sunsetWeatherIcon = userPlace.sunEvents[0].weatherIcon,
                            weatherConditions = userPlace.sunEvents[0].conditions.weatherRating,
                            blueHour = userPlace.sunEvents[0].blueHourTime.toLocalTime().format(
                                DateTimeFormatter.ofPattern("HH':'mm")
                            ),
                            goldenHour = userPlace.sunEvents[0].goldenHourTime.toLocalTime()
                                .format(
                                    DateTimeFormatter.ofPattern("HH':'mm")
                                ),
                            userLocationReady = true
                        )
                    }
                }
                else {
                    _homeUiState.update { currentHomeUiState->
                        currentHomeUiState.copy(
                            showSnackbar = true,
                            errorMessage = context.getString(R.string.error_message_getting_placeinfo)
                        )
                    }
                }

                    /*Log.d(logTag, "fetching sunsetweather")
                    val sunsetWeather = placeInfo.getLocalSunsetWeather(
                        lat = _homeUiState.value.lat,
                        long = _homeUiState.value.long)
                    // Adding try/catch to handle date missing
                    // Add to snackbar?
                    val sunsetWeatherDateTime: List<String> = try {
                        sunsetWeather.time.toString().split("T")
                    } catch (e: Exception) {
                        Log.e(logTag, "Failed fetching date",e)
                        listOf("", "")
                    }
                    val sunsetTime = sunsetWeatherDateTime[1]
                    val sunsetDate = sunsetWeatherDateTime[0]
                    // REMEMBER IS NULLABLE
                    val sunsetWeatherIcon = sunsetWeather.icon
                    currenthomeUiState.copy(
                        sunsetTime = sunsetTime,
                        sunsetDate = sunsetDate,
                        sunsetWeatherIcon = sunsetWeatherIcon,
                        date = LocalDate.now(),
                        temp = sunsetWeather.instant.air_temperature.toString(),
                        weatherConditions = placeInfo.getWeatherConditions(sunsetWeather).weatherRating
                    )*/
            } catch (e: Exception) {
                Log.e(logTag, "Error getting sunset, failed updating state", e)
                _homeUiState.update { currenthomeUiState ->
                    currenthomeUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_getting_sunset)
                    )
                }
            }
        }
    }

    /**
     * Set showSnackbar to false, so when the snackbar refresh it will be shown again
     */
    fun snackbarDismissed() {
        _homeUiState.update { currentMapUiState ->
            currentMapUiState.copy(
                showSnackbar = false,
            )
        }
    }

    /**
     *  This function refresh loadPlaceInfo when you use snackbar in MapListScreen:
     */
    fun refresh() {
        _homeUiState.update { currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch(Dispatchers.IO) {
            loadUserLocation()
            loadFavourites()
            updateWeather()
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ApplicationSkumring

                return HomeViewModel(
                    placeRepository = application.dbRepository,
                    context = application.context
                ) as T
            }
        }
    }

    fun updateBlueHourIcon(): Int {
        return when (getCurrentSystemTheme(context)) {
            Theme.DARK_MODE -> blueHourIconDarkMode
            Theme.LIGHT_MODE -> blueHourIconLightMode
            else -> blueHourIconDarkMode
        }
    }

    private fun getCurrentSystemTheme(context: Context): Theme {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> Theme.DARK_MODE
            else -> Theme.LIGHT_MODE
        }
    }
}