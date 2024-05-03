package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.ListCard
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar

private const val logTag = "FavoritesScreen"

object FavoritesDestination : NavigationDestination {
    override val icon = Icons.Outlined.FavoriteBorder
    override val buttonTitle = R.string.nav_fav_button
    override val route = "favorite"
    override val titleRes = R.string.favourites
}

/**
 * Main composable function for displaying the map screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController : NavHostController, favoritesViewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory)) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val favoritesUiState: FavoritesUiState by favoritesViewModel.favoritesUiState.collectAsState()

    //Variable for using strings in not-composable
    val context = LocalContext.current

    // Load the list of favourites every time the user navigates to this screen
    LaunchedEffect(Unit) {
        favoritesViewModel.loadList()
    }

    // Check if there is an error, if so show a snackbar:
    if (favoritesUiState.showSnackbar) {
        LaunchedEffect(favoritesUiState.snackbarHostState) {
            val result = favoritesUiState.snackbarHostState.showSnackbar(
                message = favoritesUiState.errorMessage,
                withDismissAction = true,
                actionLabel = context.getString(R.string.refresh),
            )

            // If the snackbar is dismissed, reset the boolean of the error-variable
            // The snackbar will reappear is we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {

                    favoritesViewModel.refreshList()
                }
                // If you click somewhere on the screen
                SnackbarResult.Dismissed -> {
                    // Check if in map or list
                    favoritesViewModel.snackbarDismissed()
                }
            }
        }
    }
    Scaffold (
        topBar = {
            SkumringTopBar(
                title = stringResource(id = FavoritesDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            SkumringBottomBar(navController = navController)
        }
    ) {innerPadding ->
        Column (modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            FavoriteListContent(
                navController = navController,
                favoriteViewModel = favoritesViewModel,
                favoritesUiState = favoritesUiState)
        }
    }
}

/**
 * Show the list of cards of places available
 */
@Composable
fun FavoriteListContent(navController : NavController,
                        favoriteViewModel: FavoritesViewModel,
                        favoritesUiState: FavoritesUiState
                        ) {
    Column (Modifier.verticalScroll(rememberScrollState())) {
        if (favoritesUiState.places.isEmpty()){
            Text(text = stringResource(R.string.no_places))
        } else {
            favoritesUiState.places.forEach { place ->
                ListCard(
                    name = place.name,
                    description = place.description,
                    isFavourite = place.isFavourite,
                    isCustom = place.isCustomPlace,
                    onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                        navController.navigate("placeinfoscreen/${place.id}")
                    },
                    onFavouriteClick = {favoriteViewModel.toggleFavourite(place = place)
                    },

                    //TODO gjor dynamisk slik at den henter ID
                    imageToDisplay = if (place.isCustomPlace and place.isFavourite) {
                        //TODO hente bilde fra bildedatabasen
                        "" //endres, kun satt for at det ikke krasjer
                    } else if (place.isFavourite) {
                        "${place.id}.jpg"
                    } else {
                        //TODO // If isFavourite is false it should not be displayed
                       ""
                    },
                    weatherConditionsRating = place.sunEvents[0].conditions.weatherRating
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewFavorites(navController: NavHostController = rememberNavController()) {
    FavoritesScreen(navController = navController)
}