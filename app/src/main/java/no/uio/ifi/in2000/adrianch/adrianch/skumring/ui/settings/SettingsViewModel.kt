package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ApplicationSkumring
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.settings.Settings
import java.io.File

/**
 * Ui state for settings
 * Variables for notifications, theme, language, defaultlocation and snackbar
 */
data class SettingsUiState(

    // Variables for choosing notification on/off:
    var notificationEnabled: Boolean = false,

    var settings: Settings = Settings(
        Theme.FOLLOW_SYSTEM,
        Language.FOLLOW_SYSTEM,
        Location.PHONES_LOCATION
    ),

    // Variables for showing dropdown menus
    var isThemeDropdownExpanded: Boolean = false,
    var isLanguageDropdownExpanded: Boolean = false,
    var isLocationDropdownExpanded: Boolean = false,

    // Variable for displaying snackbar
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

//Used to log errors in logcat:
private const val logTag = "SettingsViewModel"

@SuppressLint("StaticFieldLeak")
class SettingsViewModel(private val context: Context) : ViewModel() {

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    /**
     * This function is used first time an instance of SettingsViewModel is made
     */

    // Functions for theme:

    /**
     * Update variables depending on which dropdownmenu-item the user click:
     * The enum-theme-variable
     * Also update the dropdownExpandedTheme to false, which will make the dropdownmenu not visible anymore
     */
    fun updateTheme(theme: Theme) {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsUiState.update { currentSettingsUiState ->
                currentSettingsUiState.copy(
                    settings = currentSettingsUiState.settings.copy(theme = theme),
                    isThemeDropdownExpanded = false
                )
            }
            updateJSONSettings()
        }
    }

    /**
     * Update the dropdownExpandedTheme boolean variable
     * if the variable is false it will be true
     * if it is true it will be false
     * When the variable is false the dropdownmenu won't be visible
     * When the variable is true the dropdownmenu will be visible
     */
    fun toggleThemeDropdown() {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsUiState.update { currentSettingsUiState ->
                currentSettingsUiState.copy(
                    isThemeDropdownExpanded = !currentSettingsUiState.isThemeDropdownExpanded
                )
            }
        }
    }

    // Functions for StartLocation

    /**
     * Update variables depending on which dropdownmenu-item the user click:
     * The enum-language-variable
     * Also update the dropdownExpandedLanguage to false, which will make the dropdownmenu not visible anymore
     */
    fun updateLanguage(language: Language) {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsUiState.update { currentSettingsUiState ->
                currentSettingsUiState.copy(
                    settings = currentSettingsUiState.settings.copy(language = language),
                    isLanguageDropdownExpanded = false
                )
            }
            updateJSONSettings()
        }
    }

    /**
     * Update the dropdownExpandedLanguage boolean variable
     * if the variable is false it will be true
     * if it is true it will be false
     * When the variable is false the dropdownmenu won't be visible
     * When the variable is true the dropdownmenu will be visible
     */
    fun toggleLanguageDropdown() {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsUiState.update { currentSettingsUiState ->
                currentSettingsUiState.copy(
                    isLanguageDropdownExpanded = !currentSettingsUiState.isLanguageDropdownExpanded
                )
            }
        }
    }

    // Functions for StartLocation:

    /**
     * Update variables depending on which dropdownmenu-item the user click:
     * The enum-location-variable
     * Also update the dropdownExpandedStartLocation to false, which will make the dropdownmenu not visible anymore
     */
    fun updateSelectedDefaultLocation(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsUiState.update { currentSettingsUiState ->
                currentSettingsUiState.copy(
                    settings = currentSettingsUiState.settings.copy(location = location),
                    isLocationDropdownExpanded = false
                )
            }
            //TODO oppdatere JSON fil
            updateJSONSettings()
        }
    }

    /**
     * Update the JSON setting file with settings held in the settingUiState
     */
    fun updateJSONSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val gson = Gson()
            val jsonSettings: String = gson.toJson(settingsUiState.value.settings)
            val settingsFileObject = File(context.filesDir,"settings.json")
            Log.d("SettingsViewModel", "JSON setting file is updated")
            settingsFileObject.writeText(jsonSettings)

        }
    }

    /**
     * Read from setting.JSON file. If the file is not existing it is created in the local storage of the file in
     * /data/data/projectfile/files.
     * Else it reads and converts the settings files into a Settings object that is used to update UiState
     */
    fun readJSONSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val gson = Gson()

            //settings.json files does not exist before this code is run so the first time
            //settings are being read the file is also created
            val settingsFileObject = File(context.filesDir,"settings.json")

            if (!settingsFileObject.exists()){
                settingsFileObject.writeText(gson.toJson(settingsUiState.value.settings))
                Log.d("SettingsViewModel", "Setting file did not exist, created file at ${settingsFileObject.absolutePath} ")
            } else {
                //Reading from settings and converts it into a Settings object
                val json = settingsFileObject.readText()
                val settings: Settings = gson.fromJson(json, Settings::class.java)

                _settingsUiState.update { currentSettingsUiState ->
                    currentSettingsUiState.copy(
                        settings = settings,
                    )
                }
                Log.d("SettingsViewModel", "Read setting from")
            }
        }

    }

    /**
     * Update the dropdownExpandedStartLocation boolean variable
     * if the variable is false it will be true
     * if it is true it will be false
     * When the variable is false the dropdownmenu won't be visible
     * When the variable is true the dropdownmenu will be visible
     */
    fun toggleDefaultLocationDropdown() {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsUiState.update { currentSettingsUiState ->
                currentSettingsUiState.copy(
                    isLocationDropdownExpanded = !currentSettingsUiState.isLocationDropdownExpanded
                )
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
        _settingsUiState.update { currentSettingUiState ->
            currentSettingUiState.copy(showSnackbar = false)
        }
        viewModelScope.launch(Dispatchers.IO) {
            //TODO Load again here
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

                return SettingsViewModel(
                    context = application.context
                ) as T
            }
        }
    }
}
