package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

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
import java.time.LocalDate

data class HomeUiState(
    var date: LocalDate = LocalDate.of(2024,3,7),
    var time: String = "18:30",
    var temp: String = "25",
    var sunset: String = "19:00",
    var weatherCheck: Boolean = false,
    var weatherMessage: String = "Dårlig vær"
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
             val sunset = placeInfo.getSunset("10", "60", homeUiState.value.date)
             _homeUiState.update { currenthomeUiState ->
                 currenthomeUiState.copy(
                     sunset = sunset
                 )
             }
         }

    }


}

