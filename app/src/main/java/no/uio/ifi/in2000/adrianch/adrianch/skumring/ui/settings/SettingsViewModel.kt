package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ApplicationSkumring
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R

/**
 * Ui state for settings
 * Variables for notifications, theme, language, defaultlocation and snackbar
 */
data class SettingsUiState(

    // Variables for choosing notification on/off:
    var notificationEnabled: Boolean = false,

    // Variables for choosing theme:
    var theme: Theme = Theme.FOLLOW_SYSTEM,
    var selectedDropDownOptionTheme : String = "",
    var dropdownExpandedTheme : Boolean = false,

    // Variables for choosing language:
    var language: Language = Language.FOLLOW_SYSTEM,
    var selectedDropDownOptionLanguage: String = "",
    var dropdownExpandedLanguage: Boolean = false,

    // Variables for choosing Location:
    var location: Location = Location.PHONES_LOCATION,
    var selectedDropDownOptionLocation : String = "",
    var dropdownExpandedStartLocation: Boolean = false,

    // variables for choosing show location as
    var locationAs : LocationAs = LocationAs.WALK,
    var selectedLocationAs : String = "",
    var dropdownExpandedLocationAs: Boolean = false,

    // Variable for checking if there is an error:
    var showSnackbar: Boolean = false,
    // Variable that change according to the error message we get:
    var errorMessage: String = "",
    // Variable for snackbar:
    val snackbarHostState: SnackbarHostState = SnackbarHostState()
)

//Used to log errors in logcat:
private const val logTag = "SettingsViewModel"

@SuppressLint("StaticFieldLeak")
class SettingsViewModel (private val context: Context) : ViewModel() {

    private val _settingsUiState = MutableStateFlow(SettingsUiState())
    val settingsUiState: StateFlow<SettingsUiState> = _settingsUiState.asStateFlow()

    /**
     * This function is used first time an instance of SettingsViewModel is made
     */
    init {
        setDisplayStrings()
    }

    /**
     * Set the default variables for variables in the settings
     * This happens first time the settingsscreen start
     */
    fun setDisplayStrings() {
        viewModelScope.launch(Dispatchers.IO) {
            _settingsUiState.update { currentSettingsUiState ->
                currentSettingsUiState.copy(
                    selectedDropDownOptionTheme = context.resources.getString(R.string.follow_system),
                    selectedDropDownOptionLanguage = context.resources.getString(R.string.follow_system),
                    selectedDropDownOptionLocation = context.resources.getString(R.string.phones_location),
                    selectedLocationAs = context.resources.getString(R.string.walk)
                )
            }
        }
    }

    // Functions for theme:

    /**
     * Update variables depending on which dropdownmenu-item the user click:
     * The string-variable of theme
     * The enum-theme-variable
     * Also update the dropdownExpandedTheme to false, which will make the dropdownmenu not visible anymore
     */
    fun updateTheme(theme : Theme) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                val displayString = when(theme) {
                    Theme.FOLLOW_SYSTEM -> context.resources.getString(R.string.follow_system)
                    Theme.DARK_MODE -> context.resources.getString(R.string.dark_mode)
                    Theme.LIGHT_MODE -> context.resources.getString(R.string.light_mode)
                }
                currentSettingsUiState.copy(
                    theme = theme,
                    selectedDropDownOptionTheme = displayString,
                    dropdownExpandedTheme = false
                )
            }
        }
    }

    /**
     * Update the dropdownExpandedTheme boolean variable
     * if the variable is false it will be true
     * if it is true it will be false
     * When the variable is false the dropdownmenu won't be visible
     * When the variable is true the dropdownmenu will be visible
     */
    fun expandDropdownTheme() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    dropdownExpandedTheme = !currentSettingsUiState.dropdownExpandedTheme)
            }
        }
    }

    // Functions for StartLocation

    /**
     * Update variables depending on which dropdownmenu-item the user click:
     * The string-variable of language
     * The enum-language-variable
     * Also update the dropdownExpandedLanguage to false, which will make the dropdownmenu not visible anymore
     */
    fun updateLanguage(language : Language) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                val displayString = when(language) {
                    Language.FOLLOW_SYSTEM -> context.resources.getString(R.string.follow_system)
                    Language.ENGLISH -> context.resources.getString(R.string.english)
                    Language.NORWEGIAN -> context.resources.getString(R.string.norwegian)
                }
                currentSettingsUiState.copy(
                    language = language,
                    selectedDropDownOptionLanguage = displayString,
                    dropdownExpandedLanguage = false
                    )
            }
        }
    }

    /**
     * Update the dropdownExpandedLanguage boolean variable
     * if the variable is false it will be true
     * if it is true it will be false
     * When the variable is false the dropdownmenu won't be visible
     * When the variable is true the dropdownmenu will be visible
     */
    fun expandDropdownLanguage() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    dropdownExpandedLanguage = !currentSettingsUiState.dropdownExpandedLanguage)
            }
        }
    }

    // Functions for StartLocation:

    /**
     * Update variables depending on which dropdownmenu-item the user click:
     * The string-variable of defaultlocation
     * The enum-location-variable
     * Also update the dropdownExpandedStartLocation to false, which will make the dropdownmenu not visible anymore
     */
    fun updateSelectedDefaultLocation(location : Location) {
        viewModelScope.launch (Dispatchers.IO) {
            val displayString = when(location) {
                Location.COSTUM_LOCATION -> context.resources.getString(R.string.costum_location)
                Location.PHONES_LOCATION -> context.resources.getString(R.string.phones_location)
            }
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    location = location,
                    selectedDropDownOptionLocation = displayString,
                    dropdownExpandedStartLocation = false
                    )
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
    fun expandDropdownStartLocation() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    dropdownExpandedStartLocation = !currentSettingsUiState.dropdownExpandedStartLocation)
            }
        }
    }

    // Functions for LocationAs:

    /**
     * Update variables depending on which dropdownmenu-item the user click:
     * The string-variable of LocationAs
     * The enum-LocationAs-variable
     * Also update the dropdownExpandedLocationAs to false, which will make the dropdownmenu not visible anymore
     */
    fun updateSelectedLocationAs(locationAs: LocationAs) {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                val displayString = when (locationAs) {
                    LocationAs.WALK -> context.resources.getString(R.string.walk)
                    LocationAs.BIKE -> context.resources.getString(R.string.bike)
                    LocationAs.DRIVE -> context.resources.getString(R.string.drive)
                }
                currentSettingsUiState.copy(
                    locationAs = locationAs,
                    selectedLocationAs = displayString,
                    dropdownExpandedLocationAs = false
                )
            }
        }
    }

    /**
     * Update the dropdownExpandedLocationAs boolean variable
     * if the variable is false it will be true
     * if it is true it will be false
     * When the variable is false the dropdownmenu won't be visible
     * When the variable is true the dropdownmenu will be visible
     */
    fun expandDropdownLocationAs() {
        viewModelScope.launch (Dispatchers.IO) {
            _settingsUiState.update {currentSettingsUiState ->
                currentSettingsUiState.copy(
                    dropdownExpandedLocationAs = !currentSettingsUiState.dropdownExpandedLocationAs)
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

    @Suppress("UNCHECKED_CAST")
    companion object {
        val Factory: ViewModelProvider.Factory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ApplicationSkumring

                return SettingsViewModel(
                    context = application.context
                ) as T
            }
        }
    }
}
