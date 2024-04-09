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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.WeatherConditionsRating
import java.time.LocalDate

data class HomeUiState(
    var date: LocalDate = LocalDate.of(2000,1,1),
    var time: String = "00:00",
    var temp: String = "0",
    var sunset: String = "18:00",
    var weatherConditions: WeatherConditions = WeatherConditions(
        weatherRating = WeatherConditionsRating.POOR,
        cloudConditionHigh = CloudConditions.CLOUDY,
        cloudConditionMedium = CloudConditions.CLOUDY,
        cloudConditionLow = CloudConditions.CLOUDY,
        airCondition = AirConditions.HIGH
    ),
    var weatherCheck: Boolean = false,
    var weatherMessage: String = ""
)

private const val logTag = "HomeViewModel" //for logging

/**
 * ViewModel for HomeScreen
 */
class HomeViewModel() : ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val placeInfo: PlaceInfoRepository = PlaceInfoRepositoryImpl()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        loadHomeScreen()
        updateSunset()
    }

    fun loadHomeScreen(){
        viewModelScope.launch(Dispatchers.IO){
        //call function that updates time, temp, sunset, weatherMessage
            //the results will be used in functions in Homescreen

        }
    }


    //data to variables in parameters will come frome repository, not as parameter

    fun updateWeather(temp: String, sunset: String){
        viewModelScope.launch(Dispatchers.IO){
            _homeUiState.update{ currenthomeUiState->
                currenthomeUiState.copy(
                    temp = temp,
                    sunset = sunset)
            }
        }
    }

     fun updateSunset(){
         viewModelScope.launch(Dispatchers.IO){
             try {
                 val sunset = placeInfo.getSunset("10", "60", homeUiState.value.date)
                _homeUiState.update { currenthomeUiState ->
                    currenthomeUiState.copy(
                        sunset = sunset
                    )
                }
             } catch (e: Exception) {
                 Log.e(logTag, "Error getting sunset, failed updating state", e)
             }
         }
    }
}