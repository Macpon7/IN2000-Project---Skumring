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
        loadHomeScreen()
    }

    private fun loadHomeScreen(){
        viewModelScope.launch(Dispatchers.IO){
            //updateSunset(lat = lat, long = long)
            updateWeather(lat = lat, long = long)
        }
    }


    //data to variables in parameters will come frome repository, not as parameter

    private fun updateWeather(lat: String, long: String){
        viewModelScope.launch(Dispatchers.IO){
            try {
                _homeUiState.update{ currenthomeUiState->
                    Log.d(logTag, "fetching sunsetweather")
                    val sunsetWeather = placeInfo.getLocalSunsetWeather(lat = lat, long = long)
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
                Log.e(logTag, "Error getting weather, failed updating state", e)
            }
        }
    }

//     fun updateSunset(lat: String, long: String){
//         viewModelScope.launch(Dispatchers.IO){
//             try {
//                 val sunset = placeInfo.getSunset(lat, long, homeUiState.value.date)
//                _homeUiState.update { currentHomeUiState ->
//                    currentHomeUiState.copy(
//                        sunset = sunset,
//                    )
//                }
//             } catch (e: Exception) {
//                 Log.e(logTag, "Error getting sunset, failed updating state", e)
//             }
//         }
//    }
//    fun updateWeatherConditions() {
//
//    }
}