package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.userlocation

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class UserLocationUiState (
    val long: String = "10.718393",
    val lat: String = "59.943735"
)

private const val logTag = "UserLocationViewModel"

class UserLocationViewModel : ViewModel(){
//    private val userLocationRepository
    //---- get Current location
    // Now we have to create a variable that will hold the current location state and it will be updated with the getCurrentLocation function.

    //var currentLocation by mutableStateOf<Location?>(null)

//    fun getCurrentLocation() {
//        viewModelScope.launch(Dispatchers.IO) {
//            currentLocation = locationTracker.getCurrentLocation() // Location
//
//        }
//    }
}