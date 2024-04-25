package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

data class MyPageUiState(
    val places: List<PlaceInfo> = emptyList(),

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
    var imageUri: Uri?,
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
    var bitmap: List<Bitmap?> = emptyList(), // TODO tror ikke det er liste men usikker på hva

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

class MyPageViewModel(private val placeRepository: PlaceRepository) : ViewModel() {
    private val _myPageUiState = MutableStateFlow(MyPageUiState())
    val myPageUiState: StateFlow<MyPageUiState> = _myPageUiState

    private val _newPlaceUiState = MutableStateFlow(NewPlaceUiState())
    val newPlaceUiState : StateFlow<NewPlaceUiState> = _newPlaceUiState

    init {
        loadList()
    }

    // TODO can a place be added without a picture? This is not fixed

    /**
     * Update the imageUri variable when the picture is added in mypagescreen
     */
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

    // TODO, usikker på hvordan det skal se ut her
    /**
     * The function will update the bitmap variable of newPlaceUiState
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateBitMap(bitmap : List<Bitmap?>) {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(
                    bitmap = bitmap
                )
            }
        }
    }

    /**
     * Update the missingInfo variable to false if there are no spots missing information
     */
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

    /**
     * Update the isReady variable if all the required spots are filled in
     */
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

    /**
     * updates if location-name is missing
     */
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

    /**
     * updates if address is missing
     */
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

    /**
     * updates if description is missing
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateDescriptionsMissing(){
        _newPlaceUiState.update { currentNewPlaceUiState ->
            currentNewPlaceUiState.copy(
                descriptionsIsMissing = true,
                missingInfo = true
            )
        }
    }

    /**
     * Is updated if the locationNameIsMissing was true and now there is a location
     */
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

    /**
     * Is updated if the addressIsMissing was true and now there is an address
     */
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

    /**
     * Is updated if the descriptionIsMissing was true and now there is a description
     */
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
                    descriptions: String,
                    imageUri: Uri?
                    // TODO add bitmap?
    ) {
        val LocationObject = NewPlace(
            locationName = locationName,
            address = address,
            pickedDate = pickedDate,
            descriptions = descriptions,
            imageUri = imageUri
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
                    imageUri = null,
                    // TODO add bitmap ?
                    pickedDate = LocalDate.now(),
                    isReady = false,
                    missingInfo = false
                )
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
        viewModelScope.launch (Dispatchers.IO) {
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
    fun loadList(){
        // TODO load the list of the users customs places, will probably look similar to the list in maplistviewmodel
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun toggleFavourite(place: PlaceInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (place.isFavourite) {
                placeRepository.unmakeFavourite(id = place.id)
                _myPageUiState.update { currentMyPageUiState ->
                    currentMyPageUiState.copy(
                        places = placeRepository.getCustomPlaces()
                    )
                }
            } else {
                placeRepository.makeFavourite(id = place.id)
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
            currentMyPageUiState.copy(showSnackbar = false) }
        viewModelScope.launch (Dispatchers.IO) {
            // TODO: Add what gets updated when the API fails
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return MyPageViewModel(
                    placeRepository = (application as ApplicationSkumring).dbRepository
                ) as T
            }
        }
    }
}