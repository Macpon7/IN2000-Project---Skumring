package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar

object MyPageDestination : NavigationDestination {
    override val icon = Icons.Outlined.AccountCircle
    override val buttonTitle = R.string.nav_personal_button
    override val route = "mypage"
    override val titleRes = R.string.nav_personal_button
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(navController: NavHostController, myPageViewModel: MyPageViewModel = viewModel()) {
    val myPageUiState: MyPageUiState by myPageViewModel.myPageUiState.collectAsState()

    // Check if there is an error, if so show a snackbar:
    if (myPageUiState.showSnackbar) {
        LaunchedEffect(myPageUiState.snackbarHostState) {
            val result = myPageUiState.snackbarHostState.showSnackbar(
                message = myPageUiState.errorMessage,
                withDismissAction = true,
                actionLabel = "Refresh",
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
                actions = {
                    IconButton(
                        onClick = {
                                  //TODO, navigate to settingsScreen
                            },
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },bottomBar = {
            SkumringBottomBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = myPageUiState.snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(Icons.Filled.Add, "Add location")
            }
        }
        )
    { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column (modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          ContentMyPage(navController = navController, myPageViewModel = myPageViewModel)
        }
    }
}

/**
 * Content in mypage:
 * SettingsButton
 * LocationCard
 * AddLocationButton
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentMyPage(navController : NavController, myPageViewModel: MyPageViewModel) {

    val myPageUiState: MyPageUiState by myPageViewModel.myPageUiState.collectAsState()
    val newPlaceUiState : NewPlaceUiState by myPageViewModel.newPlaceUiState.collectAsState()

    if (myPageUiState.showNewLocationCard) { //When the user click AddLocationButton this is shown

        // DONT DO THIS, put it in viewmodel
        var locationName by remember { mutableStateOf("") }
        var chosenLocation by remember { mutableStateOf("") }
        var dateOfPicture by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }

        if (newPlaceUiState.showDatePicker) {
            DatePickerDialog(

                onDismissRequest = { /*TODO toggle datePickerError and showDatePicker*/ },
                confirmButton = { /*TODO*/ },
                dismissButton = { /*TODO*/ }

            ) {
                DatePicker(state = newPlaceUiState.datePickerState)
            }
        }


        // Lag dette som et eget card
        // Bruk surface of card skal poppe opp, sjekk maplistscreen
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            // String with name
            TextField(
                value = "",// Take variable from newPlaceUiState
                onValueChange = { locationName = it },
                label = { Text("Location") }
            )
            // Brukeren skal skrive inn addresse:
            TextField(
                value = chosenLocation,
                onValueChange = { chosenLocation = it },
                label = { Text("Choose location") }
            )
            // Time skal lages made datepicker dialog
            TextField(
                value = dateOfPicture,
                onValueChange = {  },
                modifier = Modifier.clickable(
                    enabled = true,
                    onClick = {
                        //TODO enable date picker in viewmodel with boolean
                    }
                ),
                enabled = false,
                isError = newPlaceUiState.datePickerError,
                label = { Text("Time of picture") }
            )

            // String with description
            TextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") }
            )
            Button(
                onClick = {
                    //TODO
                } ) {
                Text("Add a photo")
            }
            Button(
                onClick = { },
                modifier = Modifier.padding(vertical = 8.dp),
            ) {
                Text("Add Location")
            }
        }
    } else {
        // Need to have column in order to use spacer inside the Column to push the add-icon down on the page
        Column {
            var hasLocations = false

            Spacer(modifier = Modifier.weight(1f))

            if (hasLocations) {
                // Column for list view
                Column (Modifier.verticalScroll(rememberScrollState())) {
                    myPageUiState.places.forEach {place ->
                        LocationCard(
                            name = place.name,
                            description = place.description,
                            onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                                navController.navigate("placeinfoscreen/${place.lat}/${place.long}/${place.id}")
                            }
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.no_location),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }


            // TODO: Make as a dialogs
            if (hasLocations) {
                Text(text = R.string.new_location.toString())
            }
        }
    }
}

/**
 * Cards with information about places
 */
@Composable
fun LocationCard(name: String, description: String, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable(onClick = onItemClick) //Click to placeinfoscreen
    ){

        //Box for picture:
        Box(
            modifier = Modifier
                .height(150.dp)
                .padding(6.dp)
                .background(Color.LightGray, RoundedCornerShape((16.dp)))
                .fillMaxWidth(),
        ) {
            Text(
                text = "Image Placeholder",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        //Text for name of place
        Text(
            text = name,
            modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        //Text for description. Do we want weather condition in the future?
        Text(
            text = description,
            modifier = Modifier
                .padding(vertical = 2.dp)
                .padding(bottom = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,)
    }
}

@Composable
fun NewPlaceDialog() {
    Dialog(onDismissRequest = { /*TODO*/ }) {
        
    }
}

@Composable
@Preview
fun TestList(navController: NavHostController = rememberNavController()) {
    MyPageScreen(
        navController = navController,
        myPageViewModel = MyPageViewModel(

        )
        )
}