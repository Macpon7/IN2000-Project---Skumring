package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import java.time.LocalDate

data class HomeUiState(
    val date: LocalDate = LocalDate.of(2024,3,7),
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
class HomeViewModel(private val placeRepository: PlaceRepository) : ViewModel() {

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    // TODO add user's position
    private val long = "10.718393"
    private val lat = "59.943735"

    init {
        //loadHomeScreen()
    }


    /*private fun loadHomeScreen(){
        viewModelScope.launch(Dispatchers.IO){
            updateWeather(lat = lat, long = long)
        }
    }*/

    /*private fun updateWeather(lat: String, long: String){
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
    }*/

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