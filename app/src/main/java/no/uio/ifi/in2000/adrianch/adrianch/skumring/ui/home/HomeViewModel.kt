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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.InternetException
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings.Theme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HomeUiState(
    // Setting up dummy info, default location to OJD
    var date: LocalDate = LocalDate.of(2000, 1, 1),
    var temp: String = "N/A",
    var sunsetTime: String = "N/A",
    var sunsetDate: String = "N/A",
    var sunsetWeatherIcon: String? = null,
    var weatherConditionsRating: WeatherConditionsRating? = null,
    var blueHour: String = "N/A",
    var goldenHour: String = "N/A",
    var placeName: String = "",

    // Variable to check if we are loading weather info
    var isLoading: Boolean = true,

    // Variable to check if we need to show the intro dialog
    val showFirstTimeDialog: Boolean = true,

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

    private val geocodingRepository: GeocodingRepository = GeocodingRepositoryImpl()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    private var long: String = "0"
    private var lat: String = "0"

    private val blueHourIconLightMode = R.drawable.bluesunlightmode
    private val blueHourIconDarkMode = R.drawable.bluesundarkmode

    init {
        loadHomeScreen()
    }

    fun loadHomeScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            loadUserLocation()
            loadFavourites()
        }
    }

    private fun loadFavourites() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeUiState.update { currentHomeUiState ->
                try {
                    val favourites = placeRepository.getFavourites()

                    currentHomeUiState.copy(favoritePlaces = favourites)
                } catch (e: InternetException) {
                    currentHomeUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_no_forecast)

                        )
                } catch (e: Exception) {
                    currentHomeUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.resources.getString(R.string.error_message_getting_favourites)
                    )
                }
            }
        }
    }

    // TODO Should we just throw this into updateWeather?
    /**
     *    Updates coordinates used to ask for weather to devices' current coords
     */
    private fun loadUserLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val location = userLocationRepository.getUserLocation()

                // Shorten the amount of decimal points, so we don't fetch new weather info unless
                // the user has moved at least 111m from the currently saved position
                val newLat = "%.3f".format(location.lat.toDouble()).replace(",", ".")
                val newLong = "%.3f".format(location.long.toDouble()).replace(",", ".")
                Log.d(logTag + "LoadUserLoc", "Lat: $newLat, Long: $newLong")

                // Only fetch place name and weather data if the user's current location is not
                // the same as the user's location that we have saved
                if (newLat != lat || newLong != long) {
                    // First, save the coordinates
                    lat = newLat
                    long = newLong

                    val userPlaceName = geocodingRepository.getPlaceNameFromCoordinates(
                        lat = location.lat,
                        long = location.long
                    )

                    val userPlace = placeRepository.getUserLocationPlace(
                        lat = location.lat,
                        long = location.long
                    )


                    _homeUiState.update { currentHomeUiState ->
                        Log.d(logTag, "Updating home UI state")
                        currentHomeUiState.copy(
                            placeName = userPlaceName.placeName,
                            temp = userPlace.sunEvents[0].tempAtEvent,
                            sunsetTime = userPlace.sunEvents[0].time.toLocalTime().format(
                                DateTimeFormatter.ofPattern("HH':'mm")
                            ),
                            sunsetDate = userPlace.sunEvents[0].time.toLocalDate().format(
                                DateTimeFormatter.ISO_LOCAL_DATE
                            ),
                            sunsetWeatherIcon = userPlace.sunEvents[0].weatherIcon,
                            weatherConditionsRating = userPlace.sunEvents[0].conditions.weatherRating,
                            blueHour = userPlace.sunEvents[0].blueHourTime.toLocalTime().format(
                                DateTimeFormatter.ofPattern("HH':'mm")
                            ),
                            goldenHour = userPlace.sunEvents[0].goldenHourTime.toLocalTime()
                                .format(
                                    DateTimeFormatter.ofPattern("HH':'mm")
                                ),
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                // Practically no way this should happen
                Log.e(logTag, "Error updating user location", e)

                // If we end up here, we set the lat and long to 0 so that the if check above will be true
                lat = "0"
                long = "0"

                _homeUiState.update { currentHomeUiState ->
                    currentHomeUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_loadUserLocation)
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
        loadHomeScreen()
    }

    fun updateBlueHourIcon(): Int {
        return when (getCurrentSystemTheme(context)) {
            Theme.DARK_MODE -> blueHourIconDarkMode
            Theme.LIGHT_MODE -> blueHourIconLightMode
            else -> blueHourIconDarkMode
        }
    }

    fun hideFirstTimeDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeUiState.update { currentHomeUiState ->
                currentHomeUiState.copy(
                    showFirstTimeDialog = false
                )
            }
        }
    }

    private fun getCurrentSystemTheme(context: Context): Theme {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> Theme.DARK_MODE
            else -> Theme.LIGHT_MODE
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
}