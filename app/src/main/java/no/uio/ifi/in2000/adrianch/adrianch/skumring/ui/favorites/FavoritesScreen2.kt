package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.presetPlacesDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListContent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListToggleState
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListViewModel
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar


object FavoritesDestination : NavigationDestination {
    override val icon = Icons.Outlined.Place
    override val buttonTitle = R.string.nav_map_button
    override val route = "favorite"
    override val titleRes = R.string.app_name
}

//presetPlacesDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen2(navController : NavHostController, favoritesViewModel: FavoritesViewModel = viewModel()){

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val favoritesUiState: FavoritesUiState by favoritesViewModel.favoritesUiState.collectAsState()

    if (favoritesUiState.showSnackbar) {
        LaunchedEffect(favoritesUiState.snackbarHostState) {
            val result = favoritesUiState.snackbarHostState.showSnackbar(
                message = favoritesUiState.errorMessage,
                withDismissAction = true,
                actionLabel = "Refresh",
            )

            // If the snackbar is dismissed, reset the boolean of the error-variable
            // The snackbar will reappear is we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {
                    // Check if in map or list
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
                title = "Your favorite locations",
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
                FavoriteContent(navController)
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteContent(navController: NavHostController){
    Surface (color = MaterialTheme.colorScheme.background ){
        // Column for list view
        Column (Modifier.verticalScroll(rememberScrollState())) {
            presetPlacesDetails.forEach {place ->
                no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.ListCard(
                    name = place.name,
                    description = place.description,
                    onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                           // navController.navigate("placeinfoscreen/${place.lat}/${place.long}/${place.id}")
                   }
                )
            }
        }
    }
}






    /*
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
            FavoriteListContent(navController = navController, favoriteViewModel = favoritesViewModel)
        }
    } */



    @Composable
    fun ListCard2(name: String, description: String) {
        BoxWithConstraints {
            if (maxWidth < 400.dp) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                        //.clickable(onClick = onItemClick), //Click to infoscreen
                ) {

                    //Box for picture:
                    Box(
                        modifier = Modifier
                            .height(150.dp)
                            .background(Color.LightGray, RoundedCornerShape((0.dp)))
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Image Placeholder",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp)
                    )
                    {
                        Text(
                            text = name,
                            modifier = Modifier
                                .padding(vertical = 2.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Row {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.FavoriteBorder,
                                    contentDescription = ""
                                )
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = ""
                                )
                            }
                        }
                    }

                    //Text for description. Do we want weather condition in the future?
                    Text(
                        text = description,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .padding(bottom = 4.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                        //.clickable(onClick = onItemClick), //Click to infoscreen
                ) {

                    //Box for picture:
                    Box(
                        modifier = Modifier
                            .height(150.dp)
                            .background(Color.LightGray, RoundedCornerShape((0.dp)))
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Image Placeholder",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 8.dp)
                    )
                    {
                        Text(
                            text = name,
                            modifier = Modifier
                                .padding(vertical = 2.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Row {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.FavoriteBorder,
                                    contentDescription = ""
                                )
                            }
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = ""
                                )
                            }
                        }
                    }

                    //Text for description. Do we want weather condition in the future?
                    Text(
                        text = description,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .padding(bottom = 4.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }



@Preview
@Composable
fun favoritesScreen2(navController: NavHostController = rememberNavController()) {
   // ListCard2("stedsnavn", "beskrivelse")
    FavoritesScreen2(navController = navController)
}
