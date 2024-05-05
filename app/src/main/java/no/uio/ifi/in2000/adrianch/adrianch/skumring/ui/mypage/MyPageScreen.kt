package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs.DeletePlaceDialog
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.ListCard
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import java.time.format.DateTimeFormatter

private const val TAG = "MyPageScreen"

object MyPageDestination : NavigationDestination {
    override val icon = Icons.Outlined.AccountCircle
    override val buttonTitle = R.string.nav_personal_button
    override val route = "mypage"
    override val titleRes = R.string.nav_personal_button
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(
    navController: NavHostController,
    myPageViewModel: MyPageViewModel = viewModel(factory = MyPageViewModel.Factory)
) {

    val myPageUiState: MyPageUiState by myPageViewModel.myPageUiState.collectAsState()

    //Variable for using strings in not-composable
    val context = LocalContext.current

    // Load the list of custom places every time the user navigates to this screen
    LaunchedEffect(Unit) {
        myPageViewModel.loadList()
    }

    // Check if there is an error, if so show a snackbar:
    if (myPageUiState.showSnackbar) {
        LaunchedEffect(myPageUiState.snackbarHostState) {
            val result = myPageUiState.snackbarHostState.showSnackbar(
                message = myPageUiState.errorMessage,
                withDismissAction = true,
                actionLabel = context.getString(R.string.refresh),
            )

            // If the snackbar is dismissed, reset the boolean of the error-variable
            // The snackbar will reappear is we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {
                    myPageViewModel.refresh()
                }
                // If you click somewhere on the screen
                SnackbarResult.Dismissed -> {
                    myPageViewModel.snackbarDismissed()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            SkumringTopBar(
                title = stringResource(id = MyPageDestination.titleRes),
                canNavigateBack = false,
                // Button to navigate to settings-page
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(route = "settings")
                        },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.settings)
                        )
                    }
                }
            )
        }, bottomBar = {
            SkumringBottomBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = myPageUiState.snackbarHostState) },
        // Button to add custom locations:
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Show the form:
                    myPageViewModel.showNewForm()
                },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.add_location)
                )
            }
        }
    )
    { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top

        ) {
            ContentMyPage(
                navController = navController,
                myPageViewModel = myPageViewModel,
                myPageUiState = myPageUiState
            )
        }
    }
}

/**
 * Content of my page:
 */
@Composable
fun ContentMyPage(
    navController: NavController,
    myPageViewModel: MyPageViewModel,
    myPageUiState: MyPageUiState
) {
    if (myPageUiState.showDeleteDialog) {
        DeletePlaceDialog(
            onDismissRequest = {myPageViewModel.hideDeleteDialog()},
            onConfirmClick = {myPageViewModel.deleteCustomPlace()}
        )
    }
    Column(Modifier.verticalScroll(rememberScrollState())) {
        if (myPageUiState.places.isEmpty()) {
            Text(
                text = stringResource(R.string.no_location),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            myPageUiState.places.forEach { place ->
                ListCard(
                    place = place,
                    onItemClick = { //Navigate when it is clicked on
                        navController.navigate(route = "placeinfoscreen/${place.id}")
                    },
                    onFavouriteClick = { myPageViewModel.toggleFavourite(place = place) },
                    onDeleteClick = { myPageViewModel.showDeleteDialog(place.id) }
                )
            }
        }
    }

    //When the user click AddLocationButton this is shown
    if (myPageUiState.showNewPlaceDialog) {
        NewPlaceDialog(myPageViewModel = myPageViewModel)
    }
}

/**
 * Function that will pop up when addLocation is pressed
 * Should take in viewmodel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPlaceDialog(myPageViewModel: MyPageViewModel) {
    val newPlaceUiState: NewPlaceUiState by myPageViewModel.newPlaceUiState.collectAsState()

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
    )

    // Show when the user pick a date:
    if (newPlaceUiState.showDatePicker) {
        DatePickerDialog(onDismissRequest = { myPageViewModel.dismissDatePicker() },
            confirmButton = {
                TextButton(onClick = {
                    myPageViewModel.saveSelectedDate()
                }) {
                    Text(
                        text = stringResource(R.string.add_date),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    myPageViewModel.dismissDatePicker()
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
        myPageViewModel.hideNewForm()
        myPageViewModel.resetNewPlaceUiState()
    }) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(all = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.mypage_fill_in_fields),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        bottom = 4.dp,
                        top = 6.dp
                    ),
                    fontSize = 16.sp
                )

                // String with name
                OutlinedTextField(
                    modifier = Modifier.padding(all = 2.dp),
                    value = newPlaceUiState.locationName,
                    // Take variable from newPlaceUiState
                    onValueChange = { myPageViewModel.updateNewLocationName(it) },
                    label = {
                        Text(
                            text = "${stringResource(R.string.location_name)} *",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.location_name),
                        )
                    },
                    supportingText = {
                        if (newPlaceUiState.locationNameIsMissing) {
                            Text(
                                text = stringResource(R.string.error_location_name),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    colors = outlinedTextFieldColors,
                    isError = (newPlaceUiState.locationNameIsMissing)
                )

                // Brukeren skal skrive inn addresse:
                OutlinedTextField(
                    modifier = Modifier.padding(all = 2.dp),
                    value = newPlaceUiState.address,
                    onValueChange = { myPageViewModel.updateNewLocationAddress(it) },
                    label = { Text(text = "${stringResource(R.string.address)} *",
                        style = MaterialTheme.typography.bodyMedium) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = stringResource(id = R.string.address),
                        )
                    },
                    supportingText = {
                        if (newPlaceUiState.addressIsMissing) {
                            Text(
                                text = stringResource(R.string.error_address),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        } else if (newPlaceUiState.addressNoResults) {
                            Text(
                                text = stringResource(id = R.string.new_place_address_no_results),
                                color = MaterialTheme.colorScheme.error
                            )
                        } else if (newPlaceUiState.addressTooManyResults) {
                            Text(
                                text = stringResource(id = R.string.new_place_address_many_results),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    colors = outlinedTextFieldColors,
                    isError = newPlaceUiState.addressIsMissing
                )
                // Time skal lages made datepicker dialog
                OutlinedTextField(
                    value = newPlaceUiState.pickedDate.format(
                        DateTimeFormatter.ISO_LOCAL_DATE
                    ),
                    onValueChange = { },
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = {
                                myPageViewModel.showDatePicker()
                            }
                        )
                        .padding(all = 2.dp),
                    enabled = false,
                    readOnly = true,
                    isError = newPlaceUiState.dateTextFieldError,
                    label = {
                        Text(
                            text = stringResource(R.string.choose_date),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.DateRange,
                            contentDescription = stringResource(id = R.string.time),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        // Make it look not disabled
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        disabledIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )

                // String with description
                OutlinedTextField(
                    modifier = Modifier.padding(all = 2.dp),
                    value = newPlaceUiState.description,
                    onValueChange = { myPageViewModel.updateNewLocationDescription(it) },
                    label = {
                        Text(text = "${stringResource(R.string.description)} *",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Create,
                            contentDescription = stringResource(id = R.string.description)
                        )
                    },
                    supportingText = {
                        if (newPlaceUiState.descriptionIsMissing) {
                            Text(
                                text = stringResource(R.string.error_description),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    },
                    colors = outlinedTextFieldColors,
                    isError = newPlaceUiState.descriptionIsMissing,
                )

                // Button to add photo
                val launcher =
                    rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
                        newPlaceUiState.imageUri = uri
                        Log.d(TAG, "imageUri is $newPlaceUiState.imageUri")
                    }

                Button(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier.padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.add_photo),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = stringResource(id = R.string.add_photo),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                // Button that is pressed when the location is added:
                Button(
                    onClick = { myPageViewModel.addLocation() },
                    modifier = Modifier.padding(vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.add_location),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = stringResource(id = R.string.add_location),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                if (newPlaceUiState.missingInfo) {
                    Text(
                        text = stringResource(R.string.missing_fields),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun TestList(navController: NavHostController = rememberNavController()) {
    MyPageScreen(
        navController = navController,
        myPageViewModel = viewModel(factory = MyPageViewModel.Factory)
    )
}

@Composable
@Preview
fun PreviewNewPlaceDialog(navController: NavHostController = rememberNavController()) {
    //NewPlaceDialog(myPageViewModel = viewModel(factory = MyPageViewModel.Factory))
}