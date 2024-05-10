package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs

import android.net.Uri
import android.util.Log
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.GeocodeLocation
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private const val TAG = "NewPlaceCode"

data class NewPlaceUiState @OptIn(ExperimentalMaterial3Api::class) constructor(
    // Text fields:
    var name: String = "",
    var nameError: Boolean = false,

    var address: String = "",
    var addressError: Boolean = false,

    var description: String = "",
    var descriptionError: Boolean = false,

    // Handling address results from forward geocoding
    var addressNoResults: Boolean = false,
    var addressTooManyResults: Boolean = false,
    var addressResults: List<GeocodeLocation> = emptyList(),
    var selectedAddress: String = "",
    var showAddressDialog: Boolean = false,
    var addressDialogError: Boolean = false,

    // Variables for using phone's location or getting the coordinates from long pressing on the map
    var usePhoneLocation: Boolean = false,
    val useMapLocation: Boolean = false,

    val mapLat: String = "",
    val mapLong: String = "",

    // Date picker and text field
    var imageDate: LocalDate? = null,
    val imageDateString: String = "",
    var datePickerState: DatePickerState = DatePickerState(
        initialSelectedDateMillis = null,
        initialDisplayedMonthMillis = null,
        yearRange = DatePickerDefaults.YearRange,
        initialDisplayMode = DisplayMode.Picker
    ),
    var showDatePicker: Boolean = false,
    var datePickerError: Boolean = false,
    var dateTextFieldError: Boolean = false,

    // Image URI, which we put a default image in, and variables for if the user has chosen their own image or not
    var imageUri: Uri? = Uri.parse("android.resource://" + "no.uio.ifi.in2000.adrianch.adrianch.skumring" + "/" + R.drawable.default_place_image),
    var imageError: Boolean = false,
    var useCustomImage: Boolean = false,
)

/**
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
suspend fun onNewPlaceEvent(event: NewPlaceEvent, uiStateFlow: MutableStateFlow<NewPlaceUiState>) {
    when (event) {
        is NewPlaceEvent.SaveNewPlace -> {
            val inputsValid = validateInput(uiStateFlow = uiStateFlow)

            // If all inputs are valid, we try to save the place
            if (inputsValid) {
                val lat: String
                val long: String

                // If this dialog uses map location, get lat and long from uistate
                if (uiStateFlow.value.useMapLocation) {
                    lat = uiStateFlow.value.mapLat
                    long = uiStateFlow.value.mapLong
                // else, get coordinates of place either from phone's location or from address search
                } else if (uiStateFlow.value.usePhoneLocation) {
                    Log.d(TAG, "Trying to get user location")
                    val userLocation = event.getCoordinatesFromLocation()

                    lat = userLocation.lat
                    long = userLocation.long

                } else {
                    // Get coordinates from address
                    // Get addresses from API
                    val addresses = event.getCoordinatesFromAddress(uiStateFlow.value.address)

                    if (addresses.isEmpty()) {
                        uiStateFlow.update { currentUiState ->
                            currentUiState.copy(
                                addressError = true,
                                addressNoResults = true
                            )
                        }
                        return

                    } else if (addresses.size > 1) {
                        uiStateFlow.update { currentUiState ->
                            currentUiState.copy(
                                addressError = true,
                                addressTooManyResults = true,
                                addressResults = addresses,
                                showAddressDialog = true
                            )
                        }
                        return
                    }

                    lat = addresses.first().lat
                    long = addresses.first().long
                }

                // Add the new place to database
                Log.d(TAG, "Trying to add new custom place to DB")
                val imageDate = if (!uiStateFlow.value.useCustomImage) {
                    LocalDate.now()
                } else {
                    uiStateFlow.value.imageDate
                }

                event.addCustomPlace(
                    PlaceInfo(
                        id = 0,
                        name = uiStateFlow.value.name,
                        description = uiStateFlow.value.description,
                        lat = lat,
                        long = long,
                        isFavourite = false,
                        isCustomPlace = true,
                        hasNotification = false,
                        images = emptyList(),
                        sunEvents = emptyList()
                    ),
                    // If imageUri is null we will never get to this code
                    uiStateFlow.value.imageUri!!,
                    imageDate!!
                )
                event.hideDialog()
            }
        }

        is NewPlaceEvent.SetUseUserLocation -> {
            uiStateFlow.update { currentUiState ->
                currentUiState.copy(
                    usePhoneLocation = event.usePhoneLocation
                )
            }
        }

        NewPlaceEvent.ResetUiState -> {
            uiStateFlow.update {
                NewPlaceUiState()
            }
        }

        NewPlaceEvent.HideDatePicker -> {
            uiStateFlow.update {currentUiState ->
                currentUiState.copy(showDatePicker = false)
            }
        }

        NewPlaceEvent.SaveSelectedDate -> {
            uiStateFlow.update {currentUiState ->
                // Get currently selected date
                val dateFromPicker = currentUiState.datePickerState.selectedDateMillis

                // If it is null, the user has clicked confirm without selecting a date
                if (dateFromPicker == null) {
                    currentUiState.copy(
                        // So we set datePickerError to true
                        datePickerError = true
                    )
                } else {
                    // The value from the DatePickerState is stored in epoch, so we need to go "through"
                    // LocalDateTime to get our desired LocalDate object
                    val date = LocalDateTime.ofEpochSecond(
                        dateFromPicker / 1000, 0, ZoneOffset.UTC
                    ).toLocalDate()!!

                    // We also keep a string version of the date, in order to make displaying it to the user easier
                    val dateString = date.format(DateTimeFormatter.ISO_LOCAL_DATE)

                    // Close the date picker, save the date, make sure the datepicker is not in error state
                    currentUiState.copy(
                        datePickerError = false,
                        showDatePicker = !currentUiState.showDatePicker,
                        imageDate = date,
                        imageDateString = dateString
                    )
                }
            }
        }

        NewPlaceEvent.SetDescriptionError -> {
            uiStateFlow.update {currentUiState ->
                currentUiState.copy(descriptionError = true)
            }
        }

        NewPlaceEvent.SetImageDateError -> {
            uiStateFlow.update {currentUiState ->
                currentUiState.copy(dateTextFieldError = true)
            }
        }

        NewPlaceEvent.SetNameError -> {
            uiStateFlow.update {currentUiState ->
                currentUiState.copy(nameError = true)
            }
        }

        NewPlaceEvent.ShowDatePicker -> {
            uiStateFlow.update {currentUiState ->
                currentUiState.copy(showDatePicker = true)
            }
        }

        is NewPlaceEvent.UpdateAddress -> {
            uiStateFlow.update { currentUiState ->
                currentUiState.copy(address = event.newAddress)
            }
        }

        is NewPlaceEvent.UpdateDescription -> {
            uiStateFlow.update { currentUiState ->
                currentUiState.copy(description = event.newDescription)
            }
        }

        is NewPlaceEvent.UpdateName -> {
            uiStateFlow.update { currentUiState ->
                currentUiState.copy(name = event.newName)
            }
        }

        // This is called when the user closes the gallery, uri can be null here
        is NewPlaceEvent.UpdateImageUri -> {
            if (event.uri != null) {
                // Save the image in the ui state
                uiStateFlow.update {currentUiState ->
                    currentUiState.copy(
                        imageUri = event.uri,
                        useCustomImage = true
                    )
                }
            }
        }

        is NewPlaceEvent.SetSelectedAddress -> {
            uiStateFlow.update { currentUiState ->
                currentUiState.copy(selectedAddress = event.address)
            }
        }

        is NewPlaceEvent.ConfirmSelectedAddress -> {
            // if user hasn't selected an address, set error and do not close dialog
            if (uiStateFlow.value.selectedAddress == "") {
                uiStateFlow.update { currentUiState ->
                    currentUiState.copy(addressDialogError = true)
                }
            } else {
                // If the user has selected an address, save the new place and reset NewPlaceDialog
                uiStateFlow.update { currentUiState ->
                    currentUiState.copy(
                        addressError = false,
                        addressTooManyResults = false,
                        showAddressDialog = false
                    )
                }

                val selectedAddress = uiStateFlow.value.addressResults.find { it.placeName == uiStateFlow.value.selectedAddress }

                // Add the new place to database
                Log.d(TAG, "Trying to add new custom place to DB")
                event.addCustomPlace(
                    PlaceInfo(
                        id = 0,
                        name = uiStateFlow.value.name,
                        description = uiStateFlow.value.description,
                        lat = selectedAddress!!.lat,
                        long = selectedAddress.long,
                        isFavourite = false,
                        isCustomPlace = true,
                        hasNotification = false,
                        images = emptyList(),
                        sunEvents = emptyList()
                    ),
                    // If imageUri is null we will never get to this code
                    uiStateFlow.value.imageUri!!,
                    uiStateFlow.value.imageDate!!
                )
                event.hideDialog()
            }
        }

        NewPlaceEvent.ShowAddressesDialog -> {
            uiStateFlow.update { currentUiState ->
                currentUiState.copy(showAddressDialog = true)
            }
        }

        NewPlaceEvent.HideAddressDialog -> {
            uiStateFlow.update { currentUiState ->
                currentUiState.copy(showAddressDialog = false)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun validateInput(uiStateFlow: MutableStateFlow<NewPlaceUiState>): Boolean {
    var isNameMissing = false
    var isAddressMissing = false
    var isDescriptionMissing = false
    var isDateMissing = false
    var isImageMissing = false

    var isReady = true

    if (uiStateFlow.value.name == "") {
        isNameMissing = true
        isReady = false
    }
    if (uiStateFlow.value.address == "" && !uiStateFlow.value.usePhoneLocation && !uiStateFlow.value.useMapLocation) {
        isAddressMissing = true
        isReady = false
    }
    if (uiStateFlow.value.description == "") {
        isDescriptionMissing = true
        isReady = false
    }
    if (uiStateFlow.value.imageDate == null && uiStateFlow.value.useCustomImage) {
        isDateMissing = true
        isReady = false
    }
    if (uiStateFlow.value.imageUri == null) {
        isImageMissing = true
        isReady = false
    }

    return if (isReady) {
        Log.d(TAG, "Inputs are valid")
        uiStateFlow.update { currentUiState ->
            currentUiState.copy(
                nameError = false,
                addressError = false,
                descriptionError = false,
                dateTextFieldError = false,
                imageError = false
            )
        }
        true
    } else {
        Log.d(TAG, "Inputs are invalid")
        // If we are not ready, update all the relevant errors
        uiStateFlow.update { currentUiState ->
            currentUiState.copy(
                nameError = isNameMissing,
                addressError = isAddressMissing,
                descriptionError = isDescriptionMissing,
                dateTextFieldError = isDateMissing,
                imageError = isImageMissing
            )
        }
        false
    }
}