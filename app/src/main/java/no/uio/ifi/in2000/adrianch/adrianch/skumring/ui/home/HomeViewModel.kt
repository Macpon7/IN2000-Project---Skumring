package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.mapboxpins.MapRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditionsRating
import java.time.LocalDate

data class HomeUiState(
    val date: LocalDate = LocalDate.of(2000,1,1),
    val temp: String = "",
    val sunsetTime: String = "",
    val sunsetDate: String = "",
    val weatherConditions: WeatherConditionsRating = WeatherConditionsRating.POOR,
 )

private const val logTag = "HomeViewModel" //for logging

/**
 * ViewModel for HomeScreen
 */
class HomeViewModel: ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val placeInfo: PlaceInfoRepository = PlaceInfoRepositoryImpl()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    // TODO add users position, alternatively favourite position from database
    private val long = "10.71839307051461"
    private val lat = "59.943735106220444"

    init {
        loadHomeScreen()
    }

    private fun loadHomeScreen(){
        viewModelScope.launch(Dispatchers.IO){
            updateWeather(lat = lat, long = long)
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
                    currenthomeUiState.copy(
                        sunsetTime = sunsetTime,
                        sunsetDate = sunsetDate,
                        date = LocalDate.now(),
                        temp = sunsetWeather.instant.air_temperature.toString(),
                        weatherConditions = placeInfo.getWeatherConditions(sunsetWeather).weatherRating
                    )
                }
            } catch (e: Exception) {
                // Entire API thing failed
                Log.e(logTag, "Error getting weather, failed updating state", e)
            }
        }
    }
}