package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs.DeletePlaceDialog
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs.NewPlaceDialog
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.ListCard
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar

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
        myPageViewModel.snackbarDismissed()
        myPageViewModel.loadCustomPlaces()
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
                            contentDescription = stringResource(id = R.string.settings),
                            modifier = Modifier.size(40.dp)
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
            LargeFloatingActionButton(
                onClick = {
                    // Show the form:
                    myPageViewModel.showNewPlaceDialog()
                },
                modifier = Modifier
                    .padding(end = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary

            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.new_place_add_location),
                    tint = MaterialTheme.colorScheme.onPrimary
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

    //When the user click AddLocationButton this is shown
    if (myPageUiState.showNewPlaceDialog) {
        NewPlaceDialog(
            hideDialog = { myPageViewModel.hideNewPlaceDialog() },
            onEvent = { myPageViewModel.onNewPlaceDialogEvent(it) },
            getCoordinatesFromAddress = myPageViewModel.getCoordsFromAddress,
            getCoordinatesFromLocation = myPageViewModel.getCoordinatesFromUserLocation,
            addCustomPlace = myPageViewModel.addPlace,
            uiStateFlow = myPageViewModel.newPlaceUiState
            )
    }

    Column(Modifier.verticalScroll(rememberScrollState())) {
        if (myPageUiState.places.isEmpty()) {
            Text(
                text = stringResource(R.string.no_places),
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
                Spacer(modifier = Modifier.height(12.dp))
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