package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    val myPageUiState: MyPageUiState by myPageViewModel.myListUiState.collectAsState()

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
            )
        },bottomBar = {
            SkumringBottomBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = myPageUiState.snackbarHostState) },
        ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column (modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          ContentMyPage(navController = navController, myPageViewModel = myPageViewModel)
        }
    }
}

@Composable
fun ContentMyPage(navController : NavController, myPageViewModel: MyPageViewModel)
{

    val myPageUiState: MyPageUiState by myPageViewModel.myListUiState.collectAsState()

    Column {
        SettingsButton{
            // The action that happens when the button is pressed happens here:

            // Get send to the SettingsButton when the button is pressed

        }

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
                text = "You have no locations saved yet",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.weight(1f))


        AddLocationButton {
            // The action that happens when the button is pressed happens here:
            hasLocations = true //Show the list of locations
        }

        // TODO: Make as a dialogs
        if (hasLocations) {
            Text("Ny plassering lagt til!")
        }
    }
}

/**
 * Button to go into user-settings
 */
@Composable
fun SettingsButton(onClick: () -> Unit){
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Push the icon to the right:
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onClick() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
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

/**
 * Button for adding your own locations to my page
 * Will show a new "page" that allow you to add the location
 */
@Composable
fun AddLocationButton(onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.padding(16.dp)
    ) {
        // Push the icon to the right:
        Spacer(modifier = Modifier.weight(1f))
        FloatingActionButton(
            onClick = { onClick() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(Icons.Filled.Add, "Add location")
        }
    }
}

@Composable
@Preview
fun Test(navController: NavHostController = rememberNavController()) {
    MyPageScreen(navController = navController)
}