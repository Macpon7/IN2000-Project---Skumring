package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest.Builder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.userlocation.UserLocation
import java.time.LocalDate

data class HomeUiState(
    val date: LocalDate = LocalDate.of(2024,3,7),
    var long: String = "10.718393",
    var lat: String = "59.943735",
    val temp: String = "",
    val sunsetTime: String = "",
    val sunsetDate: String = "",
    val sunsetWeatherIcon: String? = "",
    val weatherConditions: WeatherConditionsRating = WeatherConditionsRating.POOR,

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
class HomeViewModel(placeInfoRepository: PlaceInfoRepository, context: Context): ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val placeInfo: OldPlaceInfoRepository = OldPlaceInfoRepositoryImpl()

    val userLocationRepository: UserLocationRepository = UserLocationRepositoryImpl(context = context)

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    // TODO add user's position


    init {
        loadHomeScreen()
    }



    private fun loadHomeScreen(){
        viewModelScope.launch(Dispatchers.IO){
            updateWeather(lat = homeUiState.value.lat, long = homeUiState.value.long)
            loadUserLocation()
            Log.d(logTag, "Lat: ${_homeUiState.value.lat}, long: ${_homeUiState.value.long}")
        }
    }

    private fun loadUserLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            _homeUiState.update { currentHomeUiState ->
                val userLoc = userLocationRepository.getUserLocation()
                Log.d(logTag, "Loaded user location")
                currentHomeUiState.copy(
                    lat = userLoc.lat,
                    long = userLoc.long
                )
            }
        }
    }

    private fun updateWeather(lat: String, long: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                _homeUiState.update{ currenthomeUiState->
                    Log.d(logTag, "fetching sunsetweather")
                    val sunsetWeather = placeInfo.getLocalSunsetWeather(lat = lat, long = long)
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
            loadHomeScreen()
            updateWeather(lat = homeUiState.value.lat, long = homeUiState.value.long)
        }
    }
}