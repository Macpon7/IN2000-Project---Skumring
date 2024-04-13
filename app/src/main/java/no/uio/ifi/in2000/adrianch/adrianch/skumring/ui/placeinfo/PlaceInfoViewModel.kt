package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepository
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.OldPlaceInfoRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo

private const val logTag = "PlaceInfoViewModel"

data class PlaceInfoUiState(
    var placeInfo: PlaceInfo = PlaceInfo("","","","", emptyList())

)

class PlaceInfoViewModel : ViewModel() {
    private val oldPlaceInfoRepository: OldPlaceInfoRepository = OldPlaceInfoRepositoryImpl()

    private val _placeInfoUiState = MutableStateFlow(PlaceInfoUiState())

    val placeInfoUiState: StateFlow<PlaceInfoUiState> = _placeInfoUiState.asStateFlow()

    fun loadPlaceInfo(lat: String, long: String, id: Int = 0){
        viewModelScope.launch(Dispatchers.IO){
            Log.d(logTag, "loadPlaceInfo called")
            try {
                _placeInfoUiState.update { currentPlaceInfoUiState ->
                    val placeInfoObject = oldPlaceInfoRepository.getPlaceInfo(lat, long, id)
                    currentPlaceInfoUiState.copy(placeInfo = placeInfoObject)
                }
            } catch(e: Exception) {
                Log.e(logTag, "Error getting pins", e)
            }
        }
    }
}



