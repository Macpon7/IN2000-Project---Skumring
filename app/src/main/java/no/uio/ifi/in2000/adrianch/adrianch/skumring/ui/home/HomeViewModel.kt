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

data class HomeUiState(
    var time: String = "12:00",
    var temp: Int = 25,
    var sunset: String = "18:30",
    var weatherCheck: Boolean = false,
    var weatherMessage: String = "Det er drittvær i dag"
)

private const val logTag = "HomeViewModel"

/**
 * ViewModel for HomeScreen
 */
class HomeViewModel() : ViewModel() {
    private val mapRepository = MapRepositoryImpl()
    private val placeInfo: PlaceInfoRepository = PlaceInfoRepositoryImpl()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        //loadHomeScreen()
    }

    fun loadHomeScreen(){
        viewModelScope.launch(Dispatchers.IO){

        //call function that updates time, temp, sunset, weatherMessage
            //the results will be used in functions in Homescreen

            //SunTempAndTime(){
            //    text = "Tid: 12:00\nTemperatur: 25°C",
            //}

            //SunDown(){
            //    text = "Solnedgang 18:30",
           // }

            //weatherCheck
        //
        }
    }
}