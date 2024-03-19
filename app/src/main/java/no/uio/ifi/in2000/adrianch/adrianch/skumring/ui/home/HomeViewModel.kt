package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    var time: String = "18:30",
    var temp: String = "25",
    var sunset: String = "19:00",
    var weatherCheck: Boolean = false,
    var weatherMessage: String = "Dårlig vær"
)

private const val logTag = "HomeViewModel"

/**
 * ViewModel for HomeScreen
 */
class HomeViewModel() : ViewModel() {
    //repository
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
                    }
    }
}