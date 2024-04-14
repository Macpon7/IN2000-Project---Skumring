package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepositoryImpl
import java.time.LocalDate

data class HomeUiState(
    var date: LocalDate = LocalDate.of(2024,3,7),
    var time: String = "18:30",
    var temp: String = "25",
    var sunset: String = "19:00",
    var weatherCheck: Boolean = false,
    var weatherMessage: String = "Dårlig vær",

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
class HomeViewModel() : ViewModel() {
    // Creates new instance of repository:
    private val placeInfo: PlaceInfoRepository = PlaceInfoRepositoryImpl()

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()

    init {
        loadHomeScreen()
        updateSunset()
    }

    private fun loadHomeScreen(){
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

     private fun updateSunset(){
         viewModelScope.launch(Dispatchers.IO){
             try {
                 val sunset = placeInfo.getSunset(
                     lat = "10",
                     long = "60",
                     date = homeUiState.value.date)
                _homeUiState.update { currenthomeUiState ->
                    currenthomeUiState.copy(
                        sunset = sunset
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
            updateSunset()
        }
    }
}