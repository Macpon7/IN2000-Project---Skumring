package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding.GeocodingRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.geocoding.GeocodingRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import java.time.LocalDateTime
import java.time.ZoneOffset

private const val TAG = "NewPlaceViewModel"


class NewPlaceViewModel(
    private val placeRepository: PlaceRepository,
    private val geocodingRepository: GeocodingRepository = GeocodingRepositoryImpl(),
    private val context: Context
) : ViewModel() {
    private val _newPlaceUiState = MutableStateFlow(NewPlaceUiState())
    val newPlaceUiState: StateFlow<NewPlaceUiState> = _newPlaceUiState

    var imageUri: Uri? = null

    init {

    }

    /**
     * Functions for updating NewPlaceUiState:
     *
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun resetNewPlaceUiState() {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    name = "",
                    nameError = false,
                    address = "",
                    addressError = false,
                    addressTooManyResults = false,
                    addressNoResults = false,
                    description = "",
                    descriptionError = false,
                    // TODO add bitmap ?
                    imageDate = null
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun addLocation(hideDialog: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Validate inputs
            // 2. If inputs are invalid, update UiState to reflect that
            // 3. If inputs are valid, add new place and close dialog

            _newPlaceUiState.update { currentNewPlaceUiState ->
                var isNameMissing = false
                var isDescriptionMissing = false
                var isAddressMissing = false
                var addressNoResults = false
                var addressTooManyResults = false

                var isReady = true

                // Check that input fields are good
                if (currentNewPlaceUiState.name == "") {
                    isNameMissing = true
                    isReady = false
                }
                if (currentNewPlaceUiState.description == "") {
                    isDescriptionMissing = true
                    isReady = false
                }
                if (currentNewPlaceUiState.address == "") {
                    isAddressMissing = true
                    isReady = false
                }

                val addresses =
                    geocodingRepository.getCoordinatesFromAddress(address = currentNewPlaceUiState.address)

                if (addresses.size == 0) {
                    //TODO set correct error and set error message under address field
                    addressNoResults = true
                    isReady = false
                } else if (addresses.size > 1) {
                    //TODO set correct error and set message under address field
                    addressTooManyResults = true
                    isReady = false
                }

                if (imageUri == null) {
                    isReady = false
                }

                //TODO add option to use User's current location instead of writing in an address

                if (!isReady) {
                    currentNewPlaceUiState.copy(
                        nameError = isNameMissing,
                        addressError = isAddressMissing,
                        descriptionError = isDescriptionMissing,
                        addressNoResults = addressNoResults,
                        addressTooManyResults = addressTooManyResults,
                        missingInfo = true
                    )
                } else {
                    Log.d(TAG, "Trying to add new custom place to DB")
                    val newId = placeRepository.addCustomPlace(
                        PlaceInfo(
                            id = 0,
                            name = currentNewPlaceUiState.name,
                            description = currentNewPlaceUiState.description,
                            lat = addresses.first().lat,
                            long = addresses.first().long,
                            isFavourite = false,
                            isCustomPlace = true,
                            hasNotification = false,
                            images = emptyList(),
                            sunEvents = emptyList()
                        ),
                        // If imageUri is null we will never get to this code
                        imageUri = imageUri!!,
                        imageTimestamp = currentNewPlaceUiState.imageDate!!,
                        context = context
                    )

                    hideDialog()
                    currentNewPlaceUiState.copy()
                }
            }
        }
    }


    /**
     * Update if datepicker should be shown
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun showDatePicker() {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    showDatePicker = !currentNewPlaceUiState.showDatePicker
                )
            }
        }
    }

    /**
     * Update if the datepicker is dismissed
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun dismissDatePicker() {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    dateTextFieldError = true,
                    showDatePicker = !currentNewPlaceUiState.showDatePicker
                )
            }
        }
    }

    /**
     * Function used to save the date picked in MyPageScreen of the user
     * It update the date picked
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun saveSelectedDate() {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                val dateFromPicker = currentNewPlaceUiState.datePickerState.selectedDateMillis

                if (dateFromPicker == null) {
                    currentNewPlaceUiState.copy(
                        datePickerError = true
                    )
                } else {
                    val date = LocalDateTime.ofEpochSecond(
                        dateFromPicker / 1000, 0, ZoneOffset.UTC
                    ).toLocalDate()!!
                    currentNewPlaceUiState.copy(
                        datePickerError = false,
                        showDatePicker = !currentNewPlaceUiState.showDatePicker,
                        imageDate = date
                    )
                }
            }
        }
    }

    /**
     * update the location name string in NewPlaceUiState
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateNewLocationName(locationName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(name = locationName)
            }
        }
    }

    /**
     * update the location name string in NewPlaceUiState
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateNewLocationAddress(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(address = address)
            }
        }
    }

    /**
     * update the location description string in NewPlaceUiState
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateNewLocationDescription(descriptions: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(description = descriptions)
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

                return NewPlaceViewModel(
                    placeRepository = application.dbRepository,
                    context = application.context
                ) as T
            }
        }
    }
}