package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import android.content.Context
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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import java.time.LocalDate

data class HomeUiState(
    // Setting up dummy info, default location to OJD
    val date: LocalDate = LocalDate.of(2000,1,1),
    var long: String = "10.718393",
    var lat: String = "59.943735",
    val temp: String = "",
    val sunsetTime: String = "",
    val sunsetDate: String = "",
    val sunsetWeatherIcon: String? = "",
    val weatherConditions: WeatherConditionsRating = WeatherConditionsRating.POOR,

    var favoritePlaces: List<PlaceInfo> = emptyList(),

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "No error",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

private const val logTag = "HomeViewModel" //for logging

/**
 * ViewModel for HomeScreen
 */
class HomeViewModel(private val placeRepository: PlaceRepository, context: Context) : ViewModel() {

    private val userLocationRepository: UserLocationRepository = UserLocationRepositoryImpl(context = context)

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        loadHomeScreen()
    }

    private fun loadHomeScreen(){
        viewModelScope.launch(Dispatchers.IO){
            loadFavourites()
            updateWeather()
        }
    }

    private fun loadFavourites() {
        viewModelScope.launch (Dispatchers.IO) {
            _homeUiState.update { currentHomeUiState ->
                val favourites = placeRepository.getFavourites()

                currentHomeUiState.copy(favoritePlaces = favourites)
            }
        }
    }

    // Should we just throw this into updateWeather?
    // Updates coordinates used to ask for weather to devices' current coords
    private fun loadUserLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _homeUiState.update { currentHomeUiState ->
                    val userLoc = userLocationRepository.getUserLocation()
                    Log.d(logTag + "LoadUserLoc", "Lat: ${userLoc.lat}, Long: ${userLoc.long}")
                    currentHomeUiState.copy(
                        lat = userLoc.lat,
                        long = userLoc.long
                    )
                }
            } catch (e: Exception) {
                // Practically no way this should happen
                Log.e(logTag, "Error updating user location", e)

            }
        }
    }

    private fun updateWeather(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                _homeUiState.update{ currenthomeUiState->
                    loadUserLocation()

                    Log.d(logTag, "fetching sunsetweather")
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
                    )
                }
             } catch (e: Exception) {
                 Log.e(logTag, "Error getting sunset, failed updating state", e)
                 _homeUiState.update { currenthomeUiState ->
                     currenthomeUiState.copy(
                         showSnackbar = true,
                         errorMessage = "Error getting sunset, failed updating state"
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
            currentMapUiState.copy(showSnackbar = false)
        }
    }
    /**
     *  This function refresh loadPlaceInfo when you use snackbar in MapListScreen:
     */
    fun refresh() {
        _homeUiState.update {currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch (Dispatchers.IO) {
            //loadHomeScreen()
            //updateWeather(lat = lat, long = long)
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return HomeViewModel(
                    placeRepository = (application as ApplicationSkumring).dbRepository
                ) as T
            }
        }
    }
}