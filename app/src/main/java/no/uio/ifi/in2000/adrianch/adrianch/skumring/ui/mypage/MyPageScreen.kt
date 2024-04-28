package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeDestination
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
                title = stringResource(id = HomeDestination.titleRes),
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
                            contentDescription = "Settings"
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
                    Icons.Filled.Add,
                    contentDescription = "Add location"
                )
            }
        }
    )
    { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)

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

    // Global variable for color of the text
    val TextColor: Color = MaterialTheme.colorScheme.onSurface

    // Colors for Textfield in dropdownmenu:
    val FocusedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer
    val UnfocusedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer

    Column(Modifier.verticalScroll(rememberScrollState())) {
        //Slik leser vi inn fra assets/presetImages
        //Image(BitmapFactory.decodeStream(LocalContext.current.assets.open("presetImages/holmenkollen.jpg")).asImageBitmap(), contentDescription = null)

        if (myPageUiState.places.isEmpty()) {
            Text(
                text = stringResource(R.string.no_location),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            myPageUiState.places.forEach { place ->
                ListCard(
                    name = place.name,
                    description = place.description,
                    isFavourite = place.isFavourite,
                    onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                        navController.navigate(
                            route = "placeinfoscreen/${place.id}"
                        )
                    },
                    onFavouriteClick = { myPageViewModel.toggleFavourite(place = place) }
                )
            }
        }
    }

    //When the user click AddLocationButton this is shown
    if (myPageUiState.showNewPlaceDialog) {
        NewPlaceDialog(
            myPageViewModel = myPageViewModel,
            textColor = TextColor,
            focusedContainerColor = FocusedContainerColor,
            unfocusedContainerColor = UnfocusedContainerColor
        )
    }
}

/**
 * Function that will pop up when addLocation is pressed
 * Should take in viewmodel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPlaceDialog(
    myPageViewModel: MyPageViewModel,
    textColor: Color,
    focusedContainerColor: Color,
    unfocusedContainerColor: Color
) {
    val newPlaceUiState: NewPlaceUiState by myPageViewModel.newPlaceUiState.collectAsState()

    // TODO keep the change when the phone change from standing to lying

    // Show when the user pick a date:
    if (newPlaceUiState.showDatePicker) {
        DatePickerDialog(

            onDismissRequest = { myPageViewModel.dismissDatePicker() },
            confirmButton = {
                TextButton(onClick = {
                    myPageViewModel.saveSelectedDate()
                }) {
                    Text(
                        text = stringResource(R.string.add_date),
                        color = textColor,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    myPageViewModel.dismissDatePicker()
                }) {
                    Text(
                        text = stringResource(R.string.cancel),
                        color = textColor,
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
            modifier = Modifier.padding(all = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            // String with name
            OutlinedTextField(
                value = newPlaceUiState.locationName,
                // Take variable from newPlaceUiState
                onValueChange = { myPageViewModel.updateNewLocationName(it) },
                label = { Text(stringResource(R.string.location_name)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "name",
                        tint = textColor
                    )
                },
                supportingText = {
                    if (newPlaceUiState.locationNameIsMissing) {
                        Text(
                            text = stringResource(R.string.error_location_name),
                            color = textColor,
                        )
                    }
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = focusedContainerColor,
                    unfocusedContainerColor = unfocusedContainerColor
                ),
                isError = (newPlaceUiState.locationNameIsMissing)
            )

            // Brukeren skal skrive inn addresse:
            OutlinedTextField(
                value = newPlaceUiState.address,
                onValueChange = { myPageViewModel.updateNewLocationAddress(it) },
                label = { Text(stringResource(R.string.address)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "addresse",
                        tint = textColor
                    )
                },
                supportingText = {
                    if (newPlaceUiState.addressIsMissing) {
                        Text(
                            text = stringResource(R.string.error_address),
                            color = textColor,
                        )
                    }
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = focusedContainerColor,
                    unfocusedContainerColor = unfocusedContainerColor
                ),
                isError = newPlaceUiState.addressIsMissing
            )
            // TODO logikken skjer i viewmodel, sender inn string med addresse


            /*
        // Alternative to OutlinedTextField for datepicker
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = { myPageViewModel.showDatePicker() }) {
            Text(text = newPlaceUiState.pickedDate.format(
                DateTimeFormatter.ISO_LOCAL_DATE
            ))
        }
         */

            // Time skal lages made datepicker dialog
            OutlinedTextField(
                value = newPlaceUiState.pickedDate.format(
                    DateTimeFormatter.ISO_LOCAL_DATE
                ),
                onValueChange = { },
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = {
                        myPageViewModel.showDatePicker()
                    }
                ),
                enabled = false,
                readOnly = true,
                isError = newPlaceUiState.dateTextFieldError,
                label = {
                    Text(
                        stringResource(R.string.time),
                        color = textColor,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "Date",
                        tint = textColor
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = focusedContainerColor,
                    unfocusedContainerColor = unfocusedContainerColor
                )
            )
            Spacer(modifier = Modifier.height(5.dp))

            // String with description
            OutlinedTextField(
                value = newPlaceUiState.descriptions,
                onValueChange = { myPageViewModel.updateNewLocationDescription(it) },
                label = { Text(stringResource(R.string.description)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = "description"
                    )
                },
                supportingText = {
                    if (newPlaceUiState.descriptionsIsMissing) {
                        Text(
                            text = stringResource(R.string.error_description),
                            color = textColor,
                        )
                    }
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(
                    focusedContainerColor = focusedContainerColor,
                    unfocusedContainerColor = unfocusedContainerColor
                ),
                isError = newPlaceUiState.descriptionsIsMissing
            )

            // Button to add photo
            PickImageFromGallery(myPageViewModel = myPageViewModel)

            // Button that is pressed when the location is added:
            Button(
                onClick = { myPageViewModel.addLocation() },
                modifier = Modifier.padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = stringResource(R.string.add_location),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "add place",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            if (newPlaceUiState.missingInfo) {
                Text(
                    text = stringResource(R.string.missing_fields),
                    color = textColor,
                )
            }
        }
    }
}

/**
 * In this function all the logic for adding picture happens
 */
@Composable
fun PickImageFromGallery(
    myPageViewModel: MyPageViewModel,
) {
    val newPlaceUiState: NewPlaceUiState by myPageViewModel.newPlaceUiState.collectAsState()

    val context = LocalContext.current
    // TODO usikker på hvordan bitmap skal lagres i viewmodel
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            newPlaceUiState.imageUri = uri
            Log.d(
                TAG,
                "imageUri is $newPlaceUiState.imageUri"
            )

        }

    Box() {
        newPlaceUiState.imageUri?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }

            bitmap.value?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(400.dp)
                        .padding(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

    }
    Button(
        onClick = { launcher.launch("image/*") },
        modifier = Modifier.padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = stringResource(R.string.add_photo),
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "add photo",
            tint = MaterialTheme.colorScheme.onPrimary
        )
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