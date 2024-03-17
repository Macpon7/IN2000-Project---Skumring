package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.PlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.DailyEvents
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo

data class PlaceInfoUiState(
    var placeInfo: PlaceInfo = PlaceInfo("","","","", emptyList())
)

class PlaceInfoViewModel : ViewModel() {
    private val placeInfo: PlaceInfoRepository = PlaceInfoRepositoryImpl()

    private val _placeInfoUiState = MutableStateFlow(PlaceInfoUiState())

    val placeInfoUiState: StateFlow<PlaceInfoUiState> = _placeInfoUiState.asStateFlow()

    init {
        //loadgr
    }

    private fun loadPlaceInfo(lat: String, long: String, id: Int = 0){
        viewModelScope.launch(Dispatchers.IO){
            _placeInfoUiState.update { currentPlaceInfoUiState ->
                val placeInfoObject = placeInfo.getPlaceInfo(lat, long, id)
                currentPlaceInfoUiState.copy(placeInfo = placeInfoObject)
            }
        }
    }





}



