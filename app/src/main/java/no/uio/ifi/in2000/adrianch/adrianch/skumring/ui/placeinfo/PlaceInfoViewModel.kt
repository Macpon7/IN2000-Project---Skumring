package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo

data class PlaceInfoUiState(
    var placeInfo: PlaceInfo = PlaceInfo("","","","", emptyList())
)

class PlaceInfoViewModel : ViewModel() {
    private val placeInfo: PlaceInfoRepository = PlaceInfoRepositoryImpl()

    private val _placeInfoUiState = MutableStateFlow(PlaceInfoUiState())

    val placeInfoUiState: StateFlow<PlaceInfoUiState> = _placeInfoUiState.asStateFlow()

}

/*
private val alpacaParties: AlpacaPartiesRepository = AlpacaPartiesRepositoryImpl()
// HomeScreen UI state
private val _homeScreenUiState = MutableStateFlow(HomeScreenUiState())
// Backing property to avoid state updates from other classes
val homeScre

 */