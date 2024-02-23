package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class HomeUiState(
    val title: String = "Home"
)

private const val logTag = "HomeViewModel"

/**
 * ViewModel for HomeScreen
 */
class HomeViewModel() : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())

    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()
}