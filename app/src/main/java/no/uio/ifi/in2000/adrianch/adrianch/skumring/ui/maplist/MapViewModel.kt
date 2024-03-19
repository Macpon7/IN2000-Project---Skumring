package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeUiState

data class MapUiState(
    val title: String = "Home"
)

private const val logTag = "HomeViewModel"

/**
 * ViewModel for MapScreen
 */
class MapViewModel() : ViewModel() {
    private val _homeUiState = MutableStateFlow(HomeUiState())

    val homeUiState: StateFlow<HomeUiState> = _homeUiState.asStateFlow()
}