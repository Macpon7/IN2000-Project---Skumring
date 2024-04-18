package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

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

data class NewPlaceUiState @OptIn(ExperimentalMaterial3Api::class) constructor(

    var locationName : String = "",

    // TODO add location
    // TODO get date from user
    // val dateOfImage: LocalDate,

    var descriptions : String = "",

    var datePickerState: DatePickerState = DatePickerState(
        initialSelectedDateMillis = null,
        initialDisplayedMonthMillis = null,
        yearRange = DatePickerDefaults.YearRange,
        initialDisplayMode = DisplayMode.Picker
        ),

    // Show the date picker when the user want to pick a date
    var showDatePicker : Boolean = false,

    // Show an error if the use did not pick a date
    var datePickerError : Boolean = false

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

    // Functions for updating NewPlaceUiState:

    /**
     * Update if the datepicker is shown or not
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateShowDatePicker() {
        viewModelScope.launch (Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(showDatePicker = !currentNewPlaceUiState.showDatePicker)
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
     * update the location description string in NewPlaceUiState
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun updateNewLocationDescription(descriptions: String) {
        viewModelScope.launch (Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                currentNewPlaceUiState.copy(locationName = descriptions)
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

    @OptIn(ExperimentalMaterial3Api::class)
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