package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
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


private const val TAG = "MyPageViewModel"
data class MyPageUiState(
    val places: List<PlaceInfo> = emptyList(),

    var showNewPlaceDialog : Boolean = false,
    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "",
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

class MyPageViewModel(private val placeRepository: PlaceRepository, private val context: Context) : ViewModel(){
    private val _myPageUiState = MutableStateFlow(MyPageUiState())
    val myPageUiState: StateFlow<MyPageUiState> = _myPageUiState

    private val _newPlaceUiState = MutableStateFlow(NewPlaceUiState())
    val newPlaceUiState : StateFlow<NewPlaceUiState> = _newPlaceUiState

    init {
        loadList()
    }

    // TODO can a place be added without a picture? This is not fixed


    /**
     * The function will add an image to the database by adding the path to the internal location where the image is stored
     */
    suspend fun addImage(contentUri: Uri, placeId: Int){
        Log.d(TAG,"contentUri: $contentUri, placeId is $placeId")
        val succeeded: Boolean = placeRepository.saveImageToInternalStorage(context = context, contentUri = contentUri, placeId = placeId)
        Log.d(TAG, "is image added to internal storage: $succeeded")
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

    @OptIn(ExperimentalMaterial3Api::class)
    fun addLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            _newPlaceUiState.update { currentNewPlaceUiState ->
                var isNameMissing = false
                var isDescriptionMissing = false
                var isAddressMissing = false

                var isReady = true

                // Check that input fields are good
                if (currentNewPlaceUiState.locationName == "") {
                    isNameMissing = true
                    isReady = false
                }
                if (currentNewPlaceUiState.descriptions == "") {
                    isDescriptionMissing = true
                    isReady = false
                }
                if (currentNewPlaceUiState.address == "") {
                    isAddressMissing = true
                    isReady = false
                }

                if (!isReady) {
                    currentNewPlaceUiState.copy(
                        locationNameIsMissing = isNameMissing,
                        addressIsMissing = isAddressMissing,
                        descriptionsIsMissing = isDescriptionMissing
                    )
                } else {
                    //TODO save as new place
                    //If image is not

                    Log.d(TAG, "In MyPageViewModel Uri is ${newPlaceUiState.value.imageUri}")
                    newPlaceUiState.value.imageUri?.let { addImage(contentUri = it, placeId = 0) //TODO make this the placeId that is generated for the new place
                    Log.d(TAG, "saving image")}
                    hideNewForm()
                    resetNewPlaceUiState()
                    currentNewPlaceUiState.copy()
                }
            }
        }
    }



    /**
     * Functions for updating NewPlaceUiState:
     *
     */
    @OptIn(ExperimentalMaterial3Api::class)
    fun resetNewPlaceUiState(){
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
                currentMyPageUiState.copy(showNewPlaceDialog = true)
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
                currentMyPageUiState.copy(showNewPlaceDialog = false)
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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ApplicationSkumring

                return MyPageViewModel(
                    placeRepository = application.dbRepository,
                    context = application.context
                ) as T
            }
        }
    }
}