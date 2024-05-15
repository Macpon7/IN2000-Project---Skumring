package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.WeatherIconCheck
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.WeatherIconPopUp
import java.io.File

object HomeDestination : NavigationDestination {
    //This one is used in the SkumringButtonBar to choose destination
    override val icon = Icons.Outlined.Home //Show home-icon
    override val buttonTitle =
        R.string.nav_home_button //This is in Int, have to use stringResource to get the String from string.xml
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    navController: NavHostController
) {
    val homeUiState: HomeUiState by homeViewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_NETWORK_STATE
        )
    )
    LaunchedEffect(true) {
        locationPermissions.launchMultiplePermissionRequest()
    }

    //Load the favourites every time the user navigates to this screen
    LaunchedEffect(Unit) {
        homeViewModel.loadHomeScreen()
    }

    //Variable for using strings in not-composable
    val context = LocalContext.current

    // Check if there is an error, if so show a snackbar:
    if (homeUiState.showSnackbar) {
        LaunchedEffect(homeUiState.snackbarHostState) {
            val result = homeUiState.snackbarHostState.showSnackbar(
                message = homeUiState.errorMessage,
                withDismissAction = true,
                actionLabel = context.getString(R.string.refresh),
            )
            // If the snackbar is dismissed, reset the boolean of the showSnackbar-variable
            // The snackbar will reappear is we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {
                    homeViewModel.refresh()
                }
                // If you click somewhere on the screen
                SnackbarResult.Dismissed -> {
                    homeViewModel.snackbarDismissed()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            SkumringTopBar(
                title = homeUiState.placeName,
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            SkumringBottomBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = homeUiState.snackbarHostState) },
        ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()) //makes the column scrollable
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            SunsetInfoCard(
                homeUiState = homeUiState,
                homeViewModel = homeViewModel
            )
            Text(
                text = stringResource(R.string.home_favourite_places),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 10.dp)
            )
            HorizontalInfoCardRow(
                homeUiState = homeUiState,
                navHostController = navController
            )
        }
    }
}

/**
 * An infocard in HomeScreen that shows time for sunset, sunset weather conditions, golden hour and blue hour at the users location
 */
@Composable
fun SunsetInfoCard(
    homeUiState: HomeUiState,
    homeViewModel: HomeViewModel
) { //, add goldenHourTime: String, blueHourTime: String later


    //variable for fetching the blueHourIcon based on light mode and dark mode
    val blueHourIcon = homeViewModel.updateBlueHourIcon()


    var showPopUp by remember { mutableStateOf(false) }

    val goldenHourTimeString = if (homeUiState.goldenHour == "00:00") {
        "--N/A--"
    } else {
        "${homeUiState.goldenHour} - ${homeUiState.sunsetTime}"
    }
    val blueHourTimeString = if (homeUiState.blueHour == "00:00") {
        "--N/A--"
    } else {
        "${homeUiState.sunsetTime} - ${homeUiState.blueHour}"
    }

    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Box( //Need box as an overlay over card for color gradient
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.scrim,
                            MaterialTheme.colorScheme.surfaceTint,
                            MaterialTheme.colorScheme.outlineVariant, //outlineVariant
                        )
                    )
                )
        )
        {

            //Displaying the information in the card
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.home_sunset),
                    color = MaterialTheme.colorScheme.onPrimary, //outline
                    style = MaterialTheme.typography.headlineLarge,
                )
                Box { //Sunset icon and time of sunset, in box because it needs to overlap
                    if (homeUiState.isLoading) {
                        Box (
                            modifier = Modifier.size(140.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = R.drawable.sunsetsymbol),
                            contentDescription = stringResource(id = R.string.homescreen_icon_sunset),
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(140.dp)
                        )
                    }
                    Text(
                        text = homeUiState.sunsetTime,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp, top = 125.dp)
                    )
                }
                Row( //For displaying weather conditions
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${stringResource(R.string.weather_condition)} ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                    )
                    if (homeUiState.weatherConditionsRating == null) {
                        Text(text = "N/A",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            //text changing based on weather conditions, in different textbox because of change of color
                            text = stringResource(id = homeUiState.weatherConditionsRating!!.stringResourceId),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }

                    //Clickable icon for showing more info about the weather conditions
                    Icon(
                        Icons.Default.Info,
                        contentDescription = stringResource(id = R.string.information),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .clickable { showPopUp = true }
                            .size(30.dp)
                            .padding(start = 5.dp, bottom = 10.dp)
                    )
                }
                if (homeUiState.sunsetWeatherIcon != null && homeUiState.weatherConditionsRating != null) {
                    WeatherIconCheck(
                        weatherCondition = homeUiState.sunsetWeatherIcon!!,
                        weather = homeUiState.weatherConditionsRating!!
                    ) //shows the icon that fits the weather forecast
                } else {
                    Icon( //if icon is null, "show image not found"
                        painterResource(id = R.drawable.image_not_found),
                        contentDescription = stringResource(id = R.string.homescreen_icon_cloudy),
                        tint = Color.Unspecified,
                        modifier = Modifier.size(140.dp)
                    )
                }
                Text(
                    text = "${homeUiState.temp} °C",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        // .align(Alignment.BottomCenter)
                        .padding(bottom = 5.dp)
                )
                Divider( // For dividing sunset today info from golden hour and blue hour times
                    modifier = Modifier.padding(
                        start = 18.dp,
                        end = 18.dp,
                        top = 10.dp,
                        bottom = 15.dp
                    ),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    thickness = 1.dp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        //.fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
                ) {
                    //Box for golden hour icon and time
                    Box {
                        Text(
                            text = stringResource(R.string.golden_hour),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
                            contentDescription = "yellow sun icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(
                                start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )

                        )
                        Text(
                            text = goldenHourTimeString,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )
                        )
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    //box for blue hour icon and time
                    Box {
                        Text(
                            text = stringResource(R.string.blue_hour),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            textAlign = TextAlign.Center,
                        )//Blue hour icon and time
                        Icon(
                            imageVector = ImageVector.vectorResource(id = blueHourIcon),
                            contentDescription = "blue sun icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(
                                start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )
                        )
                        Text(
                            text = blueHourTimeString,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )
                        )
                    }
                }
            }
        }
    }
    //close pop up that shows more information about weather conditions
    if (showPopUp) {
        WeatherIconPopUp(onClose = {
            showPopUp = false
        })
    }
}

/**
 * For displaying the HorizontalInfoCards in a row. When clicked, navigate to PlaceInfoScreen
 */
@Composable
fun HorizontalInfoCardRow(homeUiState: HomeUiState, navHostController: NavHostController) {
    if (homeUiState.favoritePlaces.isEmpty()) {
        Text(
            text = stringResource(R.string.no_places),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    } else {
        LazyRow {
            items(homeUiState.favoritePlaces) { place ->

                HorizontalInfoCardContent(
                    place = place,
                    onItemClick = {
                        navHostController.navigate("placeinfoscreen/${place.id}")
                    },
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                )
            }
        }
    }
}

/**
 * Infocards that shows picture of the favourite places of the user and the distance to them from the users current location
 */
@Composable
fun HorizontalInfoCardContent(
    place: PlaceInfo,
    onItemClick: () -> Unit,
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .width(220.dp)
            .height(220.dp)
            .clickable(onClick = onItemClick), //Click to infoscreen
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp
    ) {
        Box( //displays the image and the information under the image
            modifier = Modifier.fillMaxSize()
        ) {
            if (place.isCustomPlace) {
                //this is for fetching/getting images that are uploaded into internal storage
                val context = LocalContext.current
                val imageFile = File(context.filesDir, place.images[0].path)
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageFile)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                AsyncImage(
                    model = "file:///android_asset/presetImages/${place.images[0].path}",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Divider( //for dividing photo from bottom text
                modifier = Modifier.padding(top = 142.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
            Box(
                // Box for text displayed, covering the bottom half of the other box
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(0.35f) //set the height of the box
                    .background(MaterialTheme.colorScheme.secondaryContainer),

                ) {
                Text(
                    //Text for name of place
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 5.dp),
                    text = place.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Row(
                    //For displaying weather conditions and information popup
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(
                            bottom = 2.dp,
                            start = 8.dp,
                            end = 8.dp
                        )
                        .align(Alignment.BottomStart)
                ) {//Conditions at sunset
                    Text(
                        text = stringResource(R.string.weather_condition) + " ",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        //text changing based on weather conditions, in different textbox because of change of color
                        text = stringResource(id = place.sunEvents[0].conditions.weatherRating.stringResourceId),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.double_arrow),
                            contentDescription = stringResource(R.string.double_arrow),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(25.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * For testing the HomeScreen
 */
@Preview
@Composable
fun HomeScreenTest(navController: NavHostController = rememberNavController()) {
    HomeScreen(navController = navController)
}