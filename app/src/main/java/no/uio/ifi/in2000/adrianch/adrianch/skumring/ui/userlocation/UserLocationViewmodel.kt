package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.userlocation

import android.location.Location
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LocationViewModel : ViewModel(){

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