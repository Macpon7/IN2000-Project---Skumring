package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.geocoding.GeocodeLocation
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.userlocation.UserLocation
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme
import java.time.LocalDate
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction1
import kotlin.reflect.KSuspendFunction3

private const val TAG = "NewPlaceDialog"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPlaceDialog(
    hideDialog: () -> Unit,
    onEvent: (event: NewPlaceEvent) -> Unit,
    getCoordinatesFromAddress: KSuspendFunction1<String, List<GeocodeLocation>>,
    getCoordinatesFromLocation: KSuspendFunction0<UserLocation>,
    addCustomPlace: KSuspendFunction3<PlaceInfo, Uri, LocalDate, Unit>,
    uiStateFlow: StateFlow<NewPlaceUiState>
) {
    val newPlaceUiState: NewPlaceUiState by uiStateFlow.collectAsState()

    // TODO keep the change when the phone change from standing to lying

    val outlinedTextFieldColors = ExposedDropdownMenuDefaults.textFieldColors(
        focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
        focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        focusedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,

        unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
        unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,

        errorTextColor = MaterialTheme.colorScheme.onPrimary,
        errorContainerColor = MaterialTheme.colorScheme.primaryContainer,
        errorCursorColor = MaterialTheme.colorScheme.error,
        errorIndicatorColor = MaterialTheme.colorScheme.error,
        errorLeadingIconColor = MaterialTheme.colorScheme.error,

        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )

    // Show when the user pick a date:
    if (newPlaceUiState.showDatePicker) {
        DatePickerDialog(onDismissRequest = { onEvent(NewPlaceEvent.HideDatePicker) },
            confirmButton = {
                TextButton(onClick = {
                    onEvent(NewPlaceEvent.SaveSelectedDate)
                }) {
                    Text(
                        text = stringResource(R.string.ok),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onEvent(NewPlaceEvent.HideDatePicker)
                }) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        ) {
            DatePicker(state = newPlaceUiState.datePickerState)
        }
    }

    //This is the dialog for adding a new place
    Dialog(onDismissRequest = {
        onEvent(NewPlaceEvent.ResetUiState)
        hideDialog()
    }) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(all = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.new_place_fill_in_fields),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )

                // Input field for new place name
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newPlaceUiState.name,
                    // Take variable from newPlaceUiState
                    onValueChange = { onEvent(NewPlaceEvent.UpdateName(it)) },
                    label = {
                        if (newPlaceUiState.name == "") {
                            Text(
                                text = "${stringResource(R.string.new_place_location_name)}*",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.new_place_location_name),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    supportingText = {
                        Text(text = stringResource(id = R.string.new_place_required))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.new_place_location_name),
                        )
                    },
                    colors = outlinedTextFieldColors,
                    isError = (newPlaceUiState.nameError)
                )

                Row (
                    modifier = Modifier.fillMaxWidth().height(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = stringResource(id = R.string.new_place_use_phone_location),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Switch(
                        checked = newPlaceUiState.usePhoneLocation,
                        onCheckedChange = {onEvent(NewPlaceEvent.SetUseUserLocation(it))})

                }

                // input field for new place address
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newPlaceUiState.address,
                    onValueChange = { onEvent(NewPlaceEvent.UpdateAddress(it)) },
                    label = {
                        if (newPlaceUiState.address == "") {
                            Text(
                                text = "${stringResource(R.string.new_place_address)}*",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.new_place_address),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    },
                    supportingText = {
                        if (newPlaceUiState.addressNoResults) {
                            Text(text = stringResource(id = R.string.new_place_address_no_results))
                        } else if (newPlaceUiState.addressTooManyResults) {
                            Text(text = stringResource(id = R.string.new_place_address_many_results))
                        } else {
                            Text(text = stringResource(id = R.string.new_place_required))
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = stringResource(id = R.string.new_place_address),
                        )
                    },
                    enabled = !newPlaceUiState.usePhoneLocation,
                    colors = outlinedTextFieldColors,
                    isError = newPlaceUiState.addressError
                )

                // Input field for new place description
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newPlaceUiState.description,
                    onValueChange = { onEvent(NewPlaceEvent.UpdateDescription(it)) },
                    label = {
                        if (newPlaceUiState.description == "") {
                            Text(
                                text = "${stringResource(R.string.new_place_description)}*",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.new_place_description),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                    },
                    supportingText = {
                        Text(text = stringResource(id = R.string.new_place_required))
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = false,
                    maxLines = 3,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Create,
                            contentDescription = stringResource(id = R.string.new_place_description)
                        )
                    },
                    colors = outlinedTextFieldColors,
                    isError = newPlaceUiState.descriptionError,
                )

                // Button to add photo
                val launcher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                        onEvent(NewPlaceEvent.UpdateImageUri(uri = uri))
                        Log.d(TAG, "imageUri is ${newPlaceUiState.imageUri}")
                    }

                // Text for adding image, will turn red if user tries to submit without adding an image
                Text(
                    text = if (newPlaceUiState.imageUri == null) {
                        "${stringResource(id = R.string.new_place_add_photo)}*"
                    } else {
                        stringResource(id = R.string.new_place_add_photo)
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = if (newPlaceUiState.imageError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                    )
                Spacer(modifier = Modifier.height(4.dp))

                // Row containing button for adding photo and date picker
                Row {
                    Card (
                        modifier = Modifier
                            .requiredSize(80.dp)
                            .clickable(
                                enabled = true,
                                onClick = { launcher.launch("image/*") }
                            ),
                        border = BorderStroke(
                            width = 1.dp,
                            color = if (newPlaceUiState.imageError) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.colorScheme.secondaryContainer
                            }
                        ),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        if (newPlaceUiState.imageUri == null) {
                            Box (
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Add,
                                    contentDescription = stringResource(id = R.string.add_photo),
                                    tint = if (newPlaceUiState.imageError) {
                                        MaterialTheme.colorScheme.error
                                    } else {
                                        MaterialTheme.colorScheme.secondaryContainer
                                    }
                                )
                            }
                        } else {
                            AsyncImage(
                                model = newPlaceUiState.imageUri,
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Input field for photo timestamp
                    OutlinedTextField(
                        value = newPlaceUiState.imageDateString,
                        onValueChange = { },
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClick = { onEvent(NewPlaceEvent.ShowDatePicker) }
                            )
                            .fillMaxWidth()
                            .padding(start = 8.dp),
                        enabled = false,
                        readOnly = true,
                        isError = newPlaceUiState.dateTextFieldError,
                        label = {
                            if (newPlaceUiState.imageDateString == "") {
                                Text(
                                    text = "${stringResource(R.string.new_place_image_date)}*",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.new_place_image_date),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }

                        },
                        supportingText = {
                            if (newPlaceUiState.dateTextFieldError) {
                                Text(
                                    text = stringResource(id = R.string.new_place_required),
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                Text(
                                    text = stringResource(id = R.string.new_place_required),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                            }

                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = stringResource(id = R.string.time)
                            )
                        },
                        colors = if (newPlaceUiState.dateTextFieldError) {
                                ExposedDropdownMenuDefaults.textFieldColors(
                                    // Make it look not disabled
                                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    disabledTextColor = MaterialTheme.colorScheme.error,
                                    disabledIndicatorColor = MaterialTheme.colorScheme.error,
                                    disabledLabelColor = MaterialTheme.colorScheme.error,
                                    disabledLeadingIconColor = MaterialTheme.colorScheme.error
                                )
                            } else {
                                ExposedDropdownMenuDefaults.textFieldColors(
                                    // Make it look not disabled
                                    disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    disabledIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    disabledLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    disabledLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                    )
                }

                // Button to save the new location
                Button(
                    onClick = { onEvent(NewPlaceEvent.SaveNewPlace(
                        getCoordinatesFromAddress = getCoordinatesFromAddress,
                        getCoordinatesFromLocation = getCoordinatesFromLocation,
                        addCustomPlace = addCustomPlace,
                        hideDialog = hideDialog
                    )) },
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.new_place_add_location),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Preview(name = "Light mode, norsk")
@Composable
fun PreviewNewPlaceDialogLight() {
    val previewFun = PreviewFunctions()
    val testUiStateFlow = MutableStateFlow(NewPlaceUiState())

    SkumringTheme(useDarkTheme = false) {
        Surface {
            NewPlaceDialog(
                hideDialog = {},
                onEvent = {},
                getCoordinatesFromAddress = previewFun::getCoordinatesFromAddress,
                getCoordinatesFromLocation = previewFun::getCoordinatesFromLocation,
                addCustomPlace = previewFun::addCustomPlace,
                uiStateFlow = testUiStateFlow
            )
        }
    }
}

@Preview(name = "Light mode, norsk, error")
@Composable
fun PreviewNewPlaceDialogLightWithError() {
    val previewFun = PreviewFunctions()
    val testUiStateFlow = MutableStateFlow(NewPlaceUiState(
        nameError = true,
        addressError = true,
        imageError = true,
        descriptionError = true,
        dateTextFieldError = true,
        usePhoneLocation = true
    ))

    SkumringTheme(useDarkTheme = false) {
        Surface {
            NewPlaceDialog(
                hideDialog = {},
                onEvent = {},
                getCoordinatesFromAddress = previewFun::getCoordinatesFromAddress,
                getCoordinatesFromLocation = previewFun::getCoordinatesFromLocation,
                addCustomPlace = previewFun::addCustomPlace,
                uiStateFlow = testUiStateFlow
            )
        }
    }
}

@Preview(name = "Dark mode, engelsk, errors",
    locale = "en")
@Composable
fun PreviewNewPlaceDialogDark() {
    val previewFun = PreviewFunctions()
    val testUiStateFlow = MutableStateFlow(NewPlaceUiState(
        nameError = true,
        addressError = true,
        imageError = true,
        descriptionError = true,
        dateTextFieldError = true
    ))

    SkumringTheme(useDarkTheme = true) {
        Surface {
            NewPlaceDialog(
                hideDialog = {},
                onEvent = {},
                getCoordinatesFromAddress = previewFun::getCoordinatesFromAddress,
                getCoordinatesFromLocation = previewFun::getCoordinatesFromLocation,
                addCustomPlace = previewFun::addCustomPlace,
                uiStateFlow = testUiStateFlow
            )
        }
    }
}

class PreviewFunctions {
    suspend fun addCustomPlace(place: PlaceInfo, imageUri: Uri, imageTimestamp: LocalDate) {

    }

    suspend fun getCoordinatesFromAddress(address: String): List<GeocodeLocation> {
        return emptyList()
    }

    suspend fun getCoordinatesFromLocation(): UserLocation {
        return UserLocation(
            lat = "",
            long = "",
            bearing = 0.0f
        )
    }


}