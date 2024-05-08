package no.uio.ifi.in2000.adrianch.adrianch.skumring

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MainScreenUiState (
    var splashScreenReady: Boolean = true
)

class MainViewModel: ViewModel() {

    private val _mainScreenUiState = MutableStateFlow(MainScreenUiState())
    val mainScreenUiState: StateFlow<MainScreenUiState> = _mainScreenUiState

    init{
        loadSplashScreen()
    }

    fun loadSplashScreen() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(timeMillis = 1000L)
            _mainScreenUiState.update {currentnewMainScreenUiState ->
                currentnewMainScreenUiState.copy(
                    splashScreenReady = false
                )
            }
        }
    }
}