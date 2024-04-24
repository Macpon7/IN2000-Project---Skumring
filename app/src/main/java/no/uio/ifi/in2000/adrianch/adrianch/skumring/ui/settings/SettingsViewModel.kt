package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsUiState(

    var notificationEnabled: Boolean = false,

    // Variables for choosing mode:
    var selectedTheme: Theme = Theme.FOLLOW_SYSTEM,


    var language: Language = Language.FOLLOW_SYSTEM,

    var selectedDefaultLocation: Location = Location.PHONES_LOCATION,
    var selectedDropDownOptionLocation : String = "Phone's location",

    var showShow_Location_as: Show_Location_as = Show_Location_as.WALK,

    var dropdownExpandedStartLocation: Boolean = false,


    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "No error",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

//Used to log errors in logcat:
private const val logTag = "SettingsViewModel"

class SettingsViewModel : ViewModel() {

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    // Functions for dropdownmenu:

    // TODO
    fun expandDropdownStartLocation() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(dropdownExpandedStartLocation = !currentSettingsUiState.dropdownExpandedStartLocation)
            }
        }
    }

    // TODO
    fun updateNotificationEnabled(isChecked : Boolean) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(notificationEnabled = isChecked)
            }
        }
    }

    /**
     * Function for updating the string selectedMode in
     * TODO more comments
     */
    fun updateSelectedMode(theme : Theme) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(selectedTheme = theme)
            }
        }
    }

    // TODO comments
    fun updateLanguage(language : Language) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(language = language)
            }
        }
    }

    /**
     * Function to update selected default location
     * The function update the variable selectedDefaultLocation which is of the enum-class Location
     * It also update the variable option which is a string
     * The variable option is used to save the strings that is shown in the screens
     */
    fun updateSelectedDefaultLocation(location : Location, option : String) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    selectedDefaultLocation = location,
                    selectedDropDownOptionLocation = option)
            }
        }
    }

    //Functions for snackbar:

    /**
     * Set showSnackbar to false, so when the snackbar refresh it will be shown again
     */
    fun snackbarDismissed() {
        _settingsUiState.update { currentSettingUiState ->
            currentSettingUiState.copy(showSnackbar = false)
        }
    }

    /**
     *  This function refresh loadPlaceInfo when you use snackbar in MapListScreen:
     */
    fun refresh() {
        _settingsUiState.update {currentSettingUiState ->
            currentSettingUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch (Dispatchers.IO) {
            //TODO Load again here
        }
    }
}
