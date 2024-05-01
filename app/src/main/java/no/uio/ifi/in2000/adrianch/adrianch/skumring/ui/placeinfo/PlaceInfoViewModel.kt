package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ApplicationSkumring
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.directions.DirectionsRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.directions.DirectionsRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.forecast.ForecastRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.forecast.ForecastRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.TravelDurationDistance
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.userlocation.UserLocation

private const val logTag = "PlaceInfoViewModel"

data class PlaceInfoUiState(
    var placeInfo: PlaceInfo = PlaceInfo(
        id = 0,
        name = "",
        description = "",
        lat = "",
        long = "",
        isFavourite = false,
        isCustomPlace = false,
        hasNotification = false,
        images = emptyList(),
        sunEvents = emptyList(),
        ),

    var listTimesDistances: List<TravelDurationDistance> = emptyList(),

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable for if we should show loading wheel or not:
    var isLoading: Boolean = true,
    // Variable that change according to the error message we get:
    var errorMessage: String = "",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)


@SuppressLint("StaticFieldLeak")
class PlaceInfoViewModel(
    private val context: Context,
    private val placeRepository: PlaceRepository,
): ViewModel() {
    private val _placeInfoUiState = MutableStateFlow(PlaceInfoUiState())
    val placeInfoUiState: StateFlow<PlaceInfoUiState> = _placeInfoUiState.asStateFlow()

    private val directionsRepository: DirectionsRepository = DirectionsRepositoryImpl()
    private val userLocationRepository: UserLocationRepository = UserLocationRepositoryImpl(context = context)

    fun addFavourite(placeId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            placeRepository.makeFavourite(placeId)
        }
    }

    fun removeFavourite(placeId : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            placeRepository.unmakeFavourite(placeId)
        }
    }

    fun loadPlaceInfo(id: Int){
        val job = viewModelScope.launch(Dispatchers.IO){
            Log.d(logTag, "loadPlaceInfo called")
            _placeInfoUiState.update { currentPlaceInfoUiState ->
                try {
                    val placeInfoObject = placeRepository.getPlace(id)
                    currentPlaceInfoUiState.copy(
                        placeInfo = placeInfoObject,
                        isLoading = false,
                        )
                } catch(e: Exception) {
                    Log.e(logTag, "Error getting PlaceInfo object for place with id: $id", e)
                    currentPlaceInfoUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_getting_placeinfo))
                }
            }
        }
        Log.d(logTag, "Loading time and place?)")
        loadTimeDistance()
        job.invokeOnCompletion {
            Log.d(logTag, "New place in UiState: ${placeInfoUiState.value.placeInfo}")
        }
    }

    /**
     * Set showSnackbar to false, so when the snackbar refresh it will be shown again
      */
    fun snackbarDismissed() {
        _placeInfoUiState.update { currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
    }

    fun loadTimeDistance() {
        viewModelScope.launch (Dispatchers.IO) {
            val userLoc: UserLocation = userLocationRepository.getUserLocation()
            val timePlaceList = directionsRepository.getAllTravelDurationDistance(
                fromLat = userLoc.lat,
                fromLong = userLoc.long,
                toLat = _placeInfoUiState.value.placeInfo.lat,
                toLong = _placeInfoUiState.value.placeInfo.long
            )
            Log.d(logTag,timePlaceList.toString())
            _placeInfoUiState.update { currentPlaceInfoUiState ->
                currentPlaceInfoUiState.copy(
                    listTimesDistances = timePlaceList
                )
            }
        }
    }

    /**
     *  This function refresh loadPlaceInfo when you use snackbar in MapListScreen:
     */
    fun refresh(id: Int = 0 ) {
        _placeInfoUiState.update {currentMapUiState ->
            currentMapUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch (Dispatchers.IO) {
            loadPlaceInfo(id)
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])

                return PlaceInfoViewModel(
                    placeRepository = (application as ApplicationSkumring).dbRepository,
                    context = application.context
                ) as T
            }
        }
    }
}





