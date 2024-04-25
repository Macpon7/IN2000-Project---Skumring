package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherPerHour
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.WeatherIconCheck
import java.time.LocalDateTime

object HomeDestination : NavigationDestination {//This one is used in the SkumringButtonBar to choose destination
    override val icon = Icons.Outlined.Home //Show home-icon
    override val buttonTitle = R.string.nav_home_button //This is in Int, have to use stringResource to get the String from string.xml
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

    //Load information for the HomeScreen every time the user navigates here
    LaunchedEffect(Unit) {
        homeViewModel.loadHomeScreen()
    }

    Scaffold(
        topBar = {
            SkumringTopBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            SkumringBottomBar(navController = navController)
        }
    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()) //makes the column scrollable
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            SunsetInfoCard(homeUiState.sunsetTime, homeUiState.weatherConditions, homeUiState.temp, homeUiState.sunsetWeatherIcon)//add blueHourTime and goldenHourTime later
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
fun SunsetInfoCard(sunsetTime: String, weatherConditions: WeatherConditionsRating, temp: String, icon: String?) { //, add goldenHourTime: String, blueHourTime: String later
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
    ) {
        Box( //Need box as an overlay over card for color gradient
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(
                    MaterialTheme.colorScheme.scrim,
                    MaterialTheme.colorScheme.surfaceTint,
                    MaterialTheme.colorScheme.outlineVariant, //outlineVariant
                )
                  ))
        )
         {//Displaying the information in the card
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.home_sunset),
                    color = MaterialTheme.colorScheme.onPrimary, //outline
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier
                        .padding(top = 10.dp)
                )
                Box { //Sunset icon and time of sunset, in box because it needs to overlap
                    Icon(
                        painter = painterResource(id = R.drawable.sunsetsymbol),
                        contentDescription = "Sunset Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(140.dp)
                    )
                    Text(
                        text = sunsetTime,
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
                        text = stringResource(R.string.weather_condition),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                    )
                    Text( //text changing based on weather conditions, in different textbox because of change of color
                        text = " $weatherConditions",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                }
                if (icon != null) {
                    WeatherIconCheck(weatherCondition = icon) //shows the icon that fits the weather forecast
                } else {
                    Icon ( //if icon is null, "show image not found"
                        painterResource(id = R.drawable.image_not_found),
                        contentDescription = "Weather icon cloudy",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(140.dp)
                    )
                }
                        Text(
                            text = "$temp °C",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                               // .align(Alignment.BottomCenter)
                                .padding(bottom = 5.dp)
                        )
                Divider( //for dividing sunset today info from golden hour and blue hour times
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 15.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    thickness = 1.dp
                )
                    Row( //For displaying Golden hour and Blue hour times on a row
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 60.dp)

                    ) {
                        Text(
                            text = stringResource(R.string.golden_hour),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = stringResource(R.string.blue_hour),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                        )

                    }
                    Row( //for displaying time and icon for Golden hour and Blue Hour
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp, bottom = 10.dp)

                    ) {
                        Box { //Golden hour icon and time
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
                                contentDescription = "yellow sun icon",
                                tint = Color.Unspecified,
                                modifier = Modifier.padding(end = 10.dp)

                            )
                            Text(
                                text = "19:09 -20:31", //change this later to $goldenHourTime
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        }
                        Box {  //Blue hour icon and time
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.blaasol),
                                contentDescription = "blue sun icon",
                                tint = Color.Unspecified,
                            )
                            Text(
                                text = "20:31-21:05", //change this later to $blueHourTime
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        }
                    }
                    MoreDetailsButton()
            }
        }
    }
}


/**
 * Click button for more details about the sunset at the place you are at
 */
@Composable
fun MoreDetailsButton() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.tertiary)
    ) {
        Divider( //marks the division between the image and the informationpart of the button
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            thickness = 1.dp
        )
        Button(
            onClick = {
                      //TODO add correct destination for buttonClick // navController.navigate("infoscreen/${place.lat}/${place.long}/${place.id}")
            },
            shape = RectangleShape,
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp)
        ) {
            Text(
                text = stringResource(R.string.home_more_details_button),
                color = MaterialTheme.colorScheme.onTertiary,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

/**
 * For displaying the HorizontalInfoCards in a row. When clicked, navigate to PlaceInfoScreen
 */
@Composable
fun HorizontalInfoCardRow (homeUiState: HomeUiState, navHostController: NavHostController) {
    if (homeUiState.favoritePlaces.isEmpty()) {
        Text(text = "No favourites")
    } else {
        LazyRow {
            items(homeUiState.favoritePlaces) {place ->
                HorizontalInfoCardContent(
                    name = place.name,
                    distance = place.description, //, should be distance
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
fun HorizontalInfoCardContent(name: String, distance: String, onItemClick: () -> Unit, modifier: Modifier) {
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
            Image(
                painter = painterResource(id = R.drawable.sunset_picture), //Change to dynamic image later
                contentDescription = "sunset image placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )
            Divider( //for dividing photo from bottom text
                modifier = Modifier.padding(top = 142.dp),
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp
            )
            Box( // Box for text displayed, covering the bottom half of the other box
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(0.35f) //set the height of the box
                    .background(MaterialTheme.colorScheme.secondaryContainer),

                ) {
                Text( //Text for name of place
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 5.dp),
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                )
                Text(
                    text = stringResource(R.string.home_distance , distance), //Add correct distance later
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(vertical = 4.dp)
                        .padding(start = 15.dp, bottom = 4.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

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

/**
 * For testing the info cards displayed in a lazyrow on the HomeScreen
 */
@Preview
@Composable
fun TestHorizontalInfoCard(navController: NavHostController = rememberNavController()) {
    HorizontalInfoCardContent(
        name = "Hei",
        distance = "paa deg",
        onItemClick = { navController.navigate("destination_route") },
        modifier = Modifier
    )
}


/*
/**
* Function with alle the content of the homescreen
* This exclude the top- and bottomBar
*/
@Composable
fun ContentHomeScreen(time: String,
                      temp: String,
                      sunset: String,
                      weatherCondition: String
) {
Column (verticalArrangement = Arrangement.Center,
horizontalAlignment = Alignment.CenterHorizontally) {

        SunTempAndTime(time, temp)
        SunDown(sunset)
        Text(text = "The weather is: $weatherCondition")
    }
}


/**
* The sun is a picture and in front it should be text of the time and the temperature
*/


@Composable
fun SunTempAndTime(time: String, temp: String) {
Box(
modifier = Modifier
.size(150.dp) // Choose the wanted size of the picture
) {

        Image(
            painterResource(R.drawable.sol),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
        Text(
            text = "Tid: $time \nTemperatur: $temp°C",  //<------------------
            color = Color.White,
            modifier = Modifier.align(Alignment.Center) // Place the text in the middle of the sun
        )
    }
}

/**
 * Shows icon of the sun going down -> Need for MVP
 * Show text of when the sun goes down
 * Have also text of when it goes down and up for each icon
 */
@Composable
fun SunDown(sunset: String) {
    Box(
        modifier = Modifier
            .size(50.dp) // Choose the wanted size of the picture
            .background(Color.LightGray) //It doesnt show in darkmode without this, need to fix
    ) {
        Image(
            painterResource(R.drawable.solnedgang),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        )
    }
    Text(
        text = "Solnedgang $sunset",
        color = Color.Black,
        modifier = Modifier,
        fontSize = 12.sp
    )
}

 */


