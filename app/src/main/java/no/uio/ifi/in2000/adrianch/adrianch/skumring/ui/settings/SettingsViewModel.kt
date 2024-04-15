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
            // Load again here
        }
    }
}
