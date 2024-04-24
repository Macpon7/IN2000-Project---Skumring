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

/**
 * Ui state for settings
 * Variables for notifications, theme, language, defaultlocation and snackbar
 */
data class SettingsUiState(

    // Variables for choosing notification on/off:
    var notificationEnabled: Boolean = false,

    // Variables for choosing theme:
    var theme: Theme = Theme.FOLLOW_SYSTEM,
    var selectedDropDownOptionTheme : String = "Follow system",
    var dropdownExpandedTheme : Boolean = false,

    // Variables for choosing language:
    var language: Language = Language.FOLLOW_SYSTEM,
    var selectedDropDownOptionLanguage: String = "Follow system",
    var dropdownExpandedLanguage: Boolean = false,

    // Variables for choosing Location:
    var location: Location = Location.PHONES_LOCATION,
    var selectedDropDownOptionLocation : String = "Phone's location",
    var dropdownExpandedStartLocation: Boolean = false,

    // variables for choosing show location as
    var locationAs : Show_Location_as = Show_Location_as.WALK,
    var selectedLocationAs : String = "Walk",
    var dropdownExpandedLocationAs: Boolean = false,

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
    fun updateTheme(theme : Theme, option : String) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    theme = theme,
                    selectedDropDownOptionTheme = option
                )
            }
        }
    }

    fun expandDropdownTheme() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    dropdownExpandedTheme = !currentSettingsUiState.dropdownExpandedLanguage)
            }
        }
    }

    // TODO comments
    fun updateLanguage(language : Language, option : String) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    language = language,
                    selectedDropDownOptionLanguage = option)
            }
        }
    }

    fun expandDropdownLanguage() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    dropdownExpandedLanguage = !currentSettingsUiState.dropdownExpandedLanguage)
            }
        }
    }

    // TODO comments
    fun expandDropdownStartLocation() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    dropdownExpandedStartLocation = !currentSettingsUiState.dropdownExpandedStartLocation)
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
                    location = location,
                    selectedDropDownOptionLocation = option)
            }
        }
    }

    // TODO comments
    fun expandDropdownLocationAs() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    dropdownExpandedLocationAs = !currentSettingsUiState.dropdownExpandedLocationAs)
            }
        }
    }

    /**
     * Function to update selected default location
     * The function update the variable selectedDefaultLocation which is of the enum-class Location
     * It also update the variable option which is a string
     * The variable option is used to save the strings that is shown in the screens
     */
    fun updateSelectedLocationAs(locationAs: Show_Location_as, option : String) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    locationAs = locationAs,
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
