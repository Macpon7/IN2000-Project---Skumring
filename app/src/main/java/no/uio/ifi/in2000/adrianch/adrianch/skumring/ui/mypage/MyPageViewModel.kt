package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceListRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.mapboxpins.PinInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

data class MyPageUiState(
    val pins: List<PinInfo> = emptyList(),
    val places: List<PlaceSummary> = emptyList(),

    var showNewLocationCard : Boolean = false,
    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "No error",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState(),
    // If mypage has locations added they will be shown
    val showLocations : Boolean = false
)

/* TODO , lag en dataklasse som innholder alt data som brukeren legger inn
Dette skal sendes inn til placerepository
 */
data class NewPlace(
    val locationName: String,
    val address: String,
    val pickedDate: LocalDate,
    val descriptions: String,
    )


data class NewPlaceUiState @OptIn(ExperimentalMaterial3Api::class) constructor(

    var locationName: String = "",
    var locationNameIsMissing: Boolean = false,

    var address: String = "",
    var addressIsMissing: Boolean = false,

    // TODO add location
    // TODO get date from user
    // val dateOfImage: LocalDate,

    var datePickerState: DatePickerState = DatePickerState(
        initialSelectedDateMillis = null,
        initialDisplayedMonthMillis = null,
        yearRange = DatePickerDefaults.YearRange,
        initialDisplayMode = DisplayMode.Picker
        ),

    // This will not make an error since if it is not picked it will be the current date:
    var pickedDate: LocalDate = LocalDate.now(),

    var descriptions: String = "",
    var descriptionsIsMissing: Boolean = false,

    // Variables for picture:
    var imageUri: Uri? = null,
    var ListimageUri: List<Uri?> = emptyList(),
    var bitmap: List<Bitmap?> = emptyList(),

    // Show the date picker when the user want to pick a date
    var showDatePicker: Boolean = false,

    // Show an error if the use pressed ok without picking a date
    var datePickerError: Boolean = false,

    // Show an error if user closes date picker without picking a date
    var dateTextFieldError: Boolean = false,

    // Check if all the required spaces is filled in by the user
    var isReady: Boolean = false,

    // Check if something in the textfield is missing
    var missingInfo: Boolean = false
)

// TODO: Use this is the functions in viewmodel where errors can happen:
private const val logTag = "MyPageViewModel"

class MyPageViewModel : ViewModel() {

    // TODO: Make own repository for places that the user saves, use this as a placeholder meanwhile
    private val placeListRepository = PlaceListRepositoryImpl() // TODO do we use this repository?

    private val _myPageUiState = MutableStateFlow(MyPageUiState())
    val myPageUiState: StateFlow<MyPageUiState> = _myPageUiState

    private val _newPlaceUiState = MutableStateFlow(NewPlaceUiState())
    val newPlaceUiState : StateFlow<NewPlaceUiState> = _newPlaceUiState

    init {
        loadList()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateImageUri(uri : Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    imageUri = uri
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun notMissingInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    missingInfo = false
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateIsReady() {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    isReady = true
                )
            }
        }
    }

    // Functions to check if there is a field missing:

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateLocationNameMissing(){
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    locationNameIsMissing = true,
                    missingInfo = true
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateAddressMissing(){
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    addressIsMissing = true,
                    missingInfo = true
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDescriptionsMissing(){
        _newPlaceUiState.update { currentNewPlaceUiState ->
            currentNewPlaceUiState.copy(
                descriptionsIsMissing = true,
                missingInfo = true
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateLocationNameMissingFalse(){
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    locationNameIsMissing = false,
                    missingInfo = false
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateAddressMissingFalse(){
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    addressIsMissing = false,
                    missingInfo = false
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDescriptionsMissingFalse(){
        _newPlaceUiState.update { currentNewPlaceUiState ->
            currentNewPlaceUiState.copy(
                descriptionsIsMissing = false,
                missingInfo = false
            )
        }
    }

    /**
     * Function for adding location to the database
     */
    fun addLocation(locationName: String,
                    address: String,
                    pickedDate: LocalDate,
                    descriptions: String) {
        val LocationObject = NewPlace(
            locationName = locationName,
            address = address,
            pickedDate = pickedDate,
            descriptions = descriptions
            )
        // TODO add locationObject to the database
    }

    /**
     * Functions for updating NewPlaceUiState:
     *
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun refreshNewPlaceUiState(){
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    locationName = "",
                    locationNameIsMissing = false,
                    address = "",
                    addressIsMissing = false,
                    descriptions = "",
                    descriptionsIsMissing = false,
                    pickedDate = LocalDate.now(),
                    isReady = false,
                    missingInfo = false
                )
            }
        }
    }

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
     * Update if the datepicker is shown or not
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun dismissDatePicker() {
        viewModelScope.launch (Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    dateTextFieldError = true,
                    showDatePicker = !currentNewPlaceUiState.showDatePicker
                )
            }
        }
    }

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
                        dateFromPicker/1000, 0, ZoneOffset.UTC
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
        viewModelScope.launch (Dispatchers.IO) {
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
        viewModelScope.launch (Dispatchers.IO) {
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
        viewModelScope.launch (Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(descriptions = descriptions)
            }
        }
    }

    // Functions for updating MyPageUiState:

    /**
     *  The form for adding a location is added
     */
    fun showNewForm() {
        viewModelScope.launch (Dispatchers.IO) {
            _myPageUiState.update { currentMyPageUiState ->
                currentMyPageUiState.copy(showNewLocationCard = true)
            }
        }
    }

    /**
     * When a card is added the locations will be shown
     */
    fun showNewLocations() {
        viewModelScope.launch (Dispatchers.IO) {
            _myPageUiState.update { currentMyPageUiState ->
                currentMyPageUiState.copy(showLocations = true)
            }
        }
    }

    fun hideNewForm() {
        viewModelScope.launch (Dispatchers.IO) {
            _myPageUiState.update {currentMyPageUiState ->
                currentMyPageUiState.copy(showNewLocationCard = false)
            }
        }
    }

    /**
     * Load the list of places
     */
    private fun loadList(){
        // TODO load the list of the users customs places, will probably look similar to the list in maplistviewmodel
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
            currentMyPageUiState.copy(showSnackbar = false) }
        viewModelScope.launch (Dispatchers.IO) {
            // TODO: Add what gets updated when the API fails
        }
    }
}