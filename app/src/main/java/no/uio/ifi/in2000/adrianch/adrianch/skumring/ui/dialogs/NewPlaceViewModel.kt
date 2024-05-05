package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

private const val TAG = "NewPlaceViewModel"

data class NewPlaceUiState @OptIn(ExperimentalMaterial3Api::class) constructor(
    var locationName: String = "",
    var locationNameIsMissing: Boolean = false,

    var address: String = "",
    var addressIsMissing: Boolean = false,
    var addressNoResults: Boolean = false,
    var addressTooManyResults: Boolean = false,

    var datePickerState: DatePickerState = DatePickerState(
        initialSelectedDateMillis = null,
        initialDisplayedMonthMillis = null,
        yearRange = DatePickerDefaults.YearRange,
        initialDisplayMode = DisplayMode.Picker
    ),

    // This will not make an error since if it is not picked it will be the current date:
    var pickedDate: LocalDate = LocalDate.now(),

    var description: String = "",
    var descriptionIsMissing: Boolean = false,

    // Variables for picture:
    var imageUri: Uri? = null,

    // Show the date picker when the user want to pick a date
    var showDatePicker: Boolean = false,

    // Show an error if the use pressed ok without picking a date
    var datePickerError: Boolean = false,

    // Show an error if user closes date picker without picking a date
    var dateTextFieldError: Boolean = false,

    // Check if something in the textfield is missing
    var missingInfo: Boolean = false
)
class NewPlaceViewModel(
    private val placeRepository: PlaceRepository,
    private val geocodingRepository: GeocodingRepository = GeocodingRepositoryImpl(),
    private val context: Context
    ) : ViewModel() {
    private val _newPlaceUiState = MutableStateFlow(NewPlaceUiState())
    val newPlaceUiState: StateFlow<NewPlaceUiState> = _newPlaceUiState

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
                    locationName = "",
                    locationNameIsMissing = false,
                    address = "",
                    addressIsMissing = false,
                    addressTooManyResults = false,
                    addressNoResults = false,
                    description = "",
                    descriptionIsMissing = false,
                    imageUri = null,
                    // TODO add bitmap ?
                    pickedDate = LocalDate.now(),
                    missingInfo = false
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun addLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                var isNameMissing = false
                var isDescriptionMissing = false
                var isAddressMissing = false
                var addressNoResults = false
                var addressTooManyResults = false

                var isReady = true

                // Check that input fields are good
                if (currentNewPlaceUiState.locationName == "") {
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

                //TODO add option to use User's current location instead of writing in an address

                if (!isReady) {
                    currentNewPlaceUiState.copy(
                        locationNameIsMissing = isNameMissing,
                        addressIsMissing = isAddressMissing,
                        descriptionIsMissing = isDescriptionMissing,
                        addressNoResults = addressNoResults,
                        addressTooManyResults = addressTooManyResults
                    )
                } else {
                    Log.d(TAG, "Trying to add new custom place to DB")
                    val newId = placeRepository.addCustomPlace(
                        PlaceInfo(
                            id = 0,
                            name = currentNewPlaceUiState.locationName,
                            description = currentNewPlaceUiState.description,
                            lat = addresses.first().lat,
                            long = addresses.first().long,
                            isFavourite = false,
                            isCustomPlace = true,
                            hasNotification = false,
                            images = emptyList(),
                            sunEvents = emptyList()
                        )
                    )

                    Log.d(TAG, "In MyPageViewModel Uri is ${newPlaceUiState.value.imageUri}")
                    newPlaceUiState.value.imageUri?.let {
                        addImage(
                            contentUri = it,
                            placeId = newId,
                            timestamp = newPlaceUiState.value.pickedDate
                        ) //TODO make this the placeId that is generated for the new place
                        Log.d(TAG, "saving image")
                    }
                    currentNewPlaceUiState.copy()
                }
            }
        }
    }

    /**
     * The function will add an image to the database by adding the path to the internal location where the image is stored
     */
    private suspend fun addImage(contentUri: Uri, placeId: Int, timestamp: LocalDate) {
        Log.d(TAG, "contentUri: $contentUri, placeId is $placeId")
        val succeeded: Boolean = placeRepository.saveImageToInternalStorage(
            context = context,
            contentUri = contentUri,
            placeId = placeId,
            timestamp = timestamp
        )
        Log.d(TAG, "is image added to internal storage: $succeeded")
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
                        pickedDate = date
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
                currentNewPlaceUiState.copy(locationName = locationName)
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