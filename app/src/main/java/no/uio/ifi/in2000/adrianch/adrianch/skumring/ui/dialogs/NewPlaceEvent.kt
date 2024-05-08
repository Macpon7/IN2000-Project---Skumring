package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs

import android.content.Context
import android.net.Uri
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.GeocodeLocation
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import java.time.LocalDate

sealed interface NewPlaceEvent{
    data class UpdateName(val newName: String): NewPlaceEvent
    data class UpdateDescription(val newDescription: String): NewPlaceEvent
    data class UpdateAddress(val newAddress: String): NewPlaceEvent

    data object SaveSelectedDate: NewPlaceEvent
    data object ResetUiState: NewPlaceEvent
    data class SaveNewPlace(
        val getCoordinatesFromAddress: (address: String) -> List<GeocodeLocation>,
        val addCustomPlace: (place: PlaceInfo, imageUri: Uri, imageTimestamp: LocalDate, context: Context) -> Unit,
        val context: Context,
        val hideDialog: () -> Unit
    ): NewPlaceEvent

    data object ShowDatePicker: NewPlaceEvent
    data object HideDatePicker: NewPlaceEvent
    data object SetNameError: NewPlaceEvent
    data object SetDescriptionError: NewPlaceEvent
    data object SetImageDateError: NewPlaceEvent
}