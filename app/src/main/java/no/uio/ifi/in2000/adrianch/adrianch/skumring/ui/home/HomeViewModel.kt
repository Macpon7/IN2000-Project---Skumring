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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.locationforecast.WeatherPerHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditionsRating
import java.time.LocalDate
import java.time.LocalDateTime

data class HomeUiState(
    val date: LocalDate = LocalDate.of(2000,1,1),
    val time: String = "00:00",
    val temp: String = "0",
    val sunset: String = "18:00",
    val weatherConditions: WeatherConditionsRating = WeatherConditionsRating.POOR,
    //var weatherCheck: Boolean = false,
    //var weatherMessage: String = ""
    //var weatherPerHour: WeatherPerHour = WeatherPerHour()
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

    private val long = "10.71839307051461"
    private val lat = "59.943735106220444"

    init {
        updateWeather(lat = lat, long = long)
    }

    fun loadHomeScreen(){
        viewModelScope.launch(Dispatchers.IO){
            //updateSunset(lat = lat, long = long)
            //updateWeather(lat = lat, long = long)
        }
    }


    //data to variables in parameters will come frome repository, not as parameter

    fun updateWeather(lat: String, long: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                _homeUiState.update{ currenthomeUiState->
                    Log.d(logTag, "fetching sunsetweather")
                    val sunsetWeather = PlaceInfoRepositoryImpl().getLocalSunsetWeather(lat = lat, long = long)
                    Log.d(logTag, sunsetWeather.time.toString())
                    currenthomeUiState.copy(
                        sunset = sunsetWeather.time.toString(),
                        date = LocalDate.now(),
                        time = LocalDateTime.now().toString(),
                        temp = sunsetWeather.instant.air_temperature.toString(),
                        weatherConditions = PlaceInfoRepositoryImpl().getWeatherConditions(sunsetWeather).weatherRating


                    )
                }
            } catch (e: Exception) {
                Log.e(logTag, "Error getting weather, failed updating state", e)
            }
        }
    }

     fun updateSunset(lat: String, long: String){
         viewModelScope.launch(Dispatchers.IO){
             try {
                 val sunset = placeInfo.getSunset(lat, long, homeUiState.value.date)
                _homeUiState.update { currentHomeUiState ->
                    currentHomeUiState.copy(
                        sunset = sunset,
                    )
                }
             } catch (e: Exception) {
                 Log.e(logTag, "Error getting sunset, failed updating state", e)
             }
         }
    }
    fun updateWeatherConditions() {

    }
}