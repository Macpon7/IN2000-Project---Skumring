package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ApplicationSkumring
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding.GeocodingRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding.GeocodingRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.userlocation.UserLocationRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.InternetException
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs.NewPlaceEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs.NewPlaceUiState
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs.onNewPlaceEvent

private const val TAG = "MyPageViewModel"

data class MyPageUiState(
    val places: List<PlaceInfo> = emptyList(),

    var showNewPlaceDialog: Boolean = false,
    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState(),

    // Variables for deleting custom places
    var showDeleteDialog: Boolean = false,
    var deleteId: Int = 0
)

class MyPageViewModel(
    private val placeRepository: PlaceRepository,
    private val geocodingRepository: GeocodingRepository = GeocodingRepositoryImpl(),
    private val context: Context
) : ViewModel() {
    private val userLocationRepository: UserLocationRepository =
        UserLocationRepositoryImpl(context = context)

    private val _myPageUiState = MutableStateFlow(MyPageUiState())
    val myPageUiState: StateFlow<MyPageUiState> = _myPageUiState

    private val _newPlaceUiState = MutableStateFlow(NewPlaceUiState())
    val newPlaceUiState: StateFlow<NewPlaceUiState> = _newPlaceUiState

    val getCoordsFromAddress = geocodingRepository::getCoordinatesFromAddress
    val getCoordinatesFromUserLocation = userLocationRepository::getUserLocation
    val addPlace = placeRepository::addCustomPlace

    init {
        loadCustomPlaces()
    }

    fun onNewPlaceDialogEvent(event: NewPlaceEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                onNewPlaceEvent(event = event, uiStateFlow = _newPlaceUiState)
            } //If an exception occurs, hide the dialog and display the relevant message
            catch (e: InternetException) {
                hideNewPlaceDialog()
                _myPageUiState.update { currentMyPageUiState ->
                    currentMyPageUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_no_forecast)
                    )
                }
            }
            catch (e: Exception) {
                // If an exception occurs, hide the dialog and show a snackbar
                hideNewPlaceDialog()
                _myPageUiState.update { currentMyPageUiState ->
                    currentMyPageUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_new_place_dialog)
                    )
                }
            }
        }
    }

    /**
     *  The form for adding a location is added
     */
    fun showNewPlaceDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            _myPageUiState.update { currentMyPageUiState ->
                currentMyPageUiState.copy(showNewPlaceDialog = true)
            }
        }
    }

    fun hideNewPlaceDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            _myPageUiState.update { currentMyPageUiState ->
                currentMyPageUiState.copy(
                    showNewPlaceDialog = false,
                )
            }
            loadCustomPlaces()
            _newPlaceUiState.update { NewPlaceUiState() }
        }
    }

    /**
     * Load the list of places
     */
    fun loadCustomPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            _myPageUiState.update { currentMyPageUiState ->
                try {
                    currentMyPageUiState.copy(places = placeRepository.getCustomPlaces())
                } catch (e: Exception) {
                    currentMyPageUiState.copy(
                        showSnackbar = true,
                        errorMessage = context.getString(R.string.error_message_getting_custom_places)
                    )
                }
            }
        }
    }

    fun toggleFavourite(place: PlaceInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (place.isFavourite) {
                placeRepository.unmakeFavourite(placeId = place.id)
                _myPageUiState.update { currentMyPageUiState ->
                    currentMyPageUiState.copy(
                        places = placeRepository.getCustomPlaces()
                    )
                }
            } else {
                placeRepository.makeFavourite(placeId = place.id)
                _myPageUiState.update { currentMyPageUiState ->
                    currentMyPageUiState.copy(
                        places = placeRepository.getCustomPlaces()
                    )
                }
            }
        }
    }

    /**
     * showSnackbar is set to false and the snackbar disappear
     */
    fun snackbarDismissed() {
        _myPageUiState.update { currentMyPageUiState ->
            currentMyPageUiState.copy(showSnackbar = false)
        }
    }

    /**
     *    For refreshing when you use snackbar in MyPageScreen:
     */
    fun refresh() {
        _myPageUiState.update { currentMyPageUiState ->
            currentMyPageUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch(Dispatchers.IO) {
            loadCustomPlaces()
        }
    }

    fun showDeleteDialog(placeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _myPageUiState.update { currentMyPageUiState ->
                currentMyPageUiState.copy(
                    deleteId = placeId,
                    showDeleteDialog = true
                )
            }
        }
    }

    fun hideDeleteDialog() {
        viewModelScope.launch(Dispatchers.IO) {
            _myPageUiState.update { currentMyPageUiState ->
                currentMyPageUiState.copy(
                    showDeleteDialog = false
                )
            }
        }
    }

    fun deleteCustomPlace() {
        viewModelScope.launch (Dispatchers.IO) {
            placeRepository.removeCustomPlace(placeId = myPageUiState.value.deleteId)
            _myPageUiState.update { currentMyPageUiState ->
                currentMyPageUiState.copy(
                    showDeleteDialog = false,
                    places = placeRepository.getCustomPlaces())
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ApplicationSkumring

                return MyPageViewModel(
                    placeRepository = application.dbRepository,
                    context = application.context
                ) as T
            }
        }
    }
}