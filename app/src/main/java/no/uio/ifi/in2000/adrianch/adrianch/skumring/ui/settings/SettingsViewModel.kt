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

    var notificationEnabled : Boolean = false,

    // Variables for choosing mode:
    var selectedMode : String = "", // TODO skal lagres som enum

    var selectedDefaultLocation : String = "", // TODO skal lagres som enum

    var language : String = "", // TODO skal lagres som enum

    var dropdownExpandedStartLocation : Boolean = false,


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

    fun expandDropdownStartLocation() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(dropdownExpandedStartLocation = !currentSettingsUiState.dropdownExpandedStartLocation)
            }
        }
    }


    fun updateNotificationEnabled(isChecked : Boolean) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(notificationEnabled = isChecked)
            }
        }
    }

    /**
     * Function for updating the string selectedMode in SettingsUiState
     */
    fun updateSelectedMode(mode : String) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(selectedMode = mode)
            }
        }
    }

    fun updateLanguage(language : String) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(language = language)
            }
        }
    }

    fun updateSelectedDefaultLocation(location : String) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(selectedDefaultLocation = location)
            }
        }
    }

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
