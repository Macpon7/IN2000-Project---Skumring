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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.SkumringTopAppBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListUiState
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListViewModel
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.md_theme_dark_background
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.md_theme_dark_onSecondary
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.md_theme_light_primaryContainer


object HomeDestination : NavigationDestination {//This one is used in the SkumringButtonBar to choose destination
    override val icon = Icons.Outlined.Home //Show home-icon
    override val buttonTitle = R.string.nav_home_button //This is in Int, have to use stringResource to get the String from string.xml
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, homeViewModel: HomeViewModel = viewModel(), mapListViewModel: MapListViewModel = viewModel()
) {
    val homeUiState: HomeUiState by homeViewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior() //var her før
    val mapListUiState: MapListUiState by mapListViewModel.mapListUiState.collectAsState()

    var sunsetTime: String = homeUiState.sunset
    var temp: String = homeUiState.temp
    var weatherConditions: String = homeUiState.weatherMessage
    //var goldenHourTime: String //add when info is available
    //var blueHourTime: String  //add when info is available

    var weatherCheck: Boolean = homeUiState.weatherCheck
    var time: String = homeUiState.time
    // var timeUiState: String = homeUiState.time

    Scaffold(
        topBar = {
            SkumringTopAppBar(
                title = stringResource( id = HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(color = md_theme_dark_background),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            SunsetInfoCard(sunsetTime, weatherConditions, temp)
                HorizontalInfoListContent(
                    mapListUiState = mapListUiState,
                    navController = navController
                )
            }
        }
    }

/**
 * An infocard in HomeScreen that shows time for sunset, sunset weather conditions, golden hour and blue hour at the users location
 */

@Composable
fun SunsetInfoCard(sunsetTime: String, weatherConditions: String, temp: String) { //, goldenHourTime: String, blueHourTime: String
/*
primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary =
 */
    val colorStops = arrayOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.onPrimary,
         MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.onPrimaryContainer,


    )
    Card(
        //backgroundColor = Color.Transparent, //removing existing background color
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
        // .background(Brush.horizontalGradient(colorStops = colorStops)
    ) {
        Box(
            //Need box for color gradient
            modifier = Modifier
               // .fillMaxSize()
                .background(Brush.verticalGradient(listOf(
                    MaterialTheme.colorScheme.primary,
                    //MaterialTheme.colorScheme.secondary,
                   // MaterialTheme.colorScheme.surfaceTint,
                    MaterialTheme.colorScheme.onPrimaryContainer,

                )))
                        )


            //.background( //adding desired backgroundcolor
         {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sunset today",
                    color = md_theme_light_primaryContainer,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(top = 15.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.sunsetsymbol),
                    contentDescription = "Sunset Icon",
                    modifier = Modifier.size(120.dp),
                    tint = Color.Unspecified
                )
                Text(
                    text = sunsetTime,
                    style = MaterialTheme.typography.headlineLarge,
                    color = md_theme_light_primaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                )
                //Row for weather conditions
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Weather conditions:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_primaryContainer,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = " $weatherConditions", //change this later
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
//color = md_theme_dark_inversePrimary,
                        textAlign = TextAlign.Center,
                    )
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.weathersymbolcloudy),
                            contentDescription = "Weather icon cloudy",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .padding(start = 45.dp) //The icon was not in the middle
                        )
                        Text(
                            text = "$temp°C", //change this later
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = md_theme_light_primaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 5.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 70.dp)

                    ) {
                        Text(
                            text = "Golden Hour ", //change this later
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = md_theme_light_primaryContainer,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = "Blue Hour", //change this later
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = md_theme_light_primaryContainer,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 40.dp, bottom = 10.dp)

                    ) {
                        Box {
                            //Golden hour icon and time
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
                                contentDescription = "yellow sun icon",
                                tint = Color.Unspecified,
                                modifier = Modifier.padding(end = 10.dp)

                            )
                            Text(
                                text = "19:09 -20:31", //change this later to $goldenHourTime
                                style = MaterialTheme.typography.bodyMedium,
                                color = md_theme_light_primaryContainer,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        }
                        //Blue hour icon and time
                        Box {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.blaasol),
                                contentDescription = "blue sun icon",
                                tint = Color.Unspecified,
                            )
                            Text(
                                text = "20:31-21:05", //change this later to $blueHourTime
                                style = MaterialTheme.typography.bodyMedium,
                                color = md_theme_light_primaryContainer,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(start = 20.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { }, shape = RectangleShape,
                        contentPadding = PaddingValues(0.dp),
                        // colors = ButtonDefaults.buttonColors(containerColor = md_theme_dark_inversePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 0.dp, end = 0.dp)
                        // .background(md_theme_dark_inversePrimary)

                    ) {
                        Text(
                            text = "More Info",
                            fontSize = 18.sp
                        )
                    }


                }
            }
        }
    }
}


@Composable
fun HorizontalInfoListContent    (mapListUiState: MapListUiState, navController: NavController) {
    LazyRow() {
        Modifier.padding(start = 10.dp, end = 10.dp)
        items(mapListUiState.places) {place ->
            HorizontalInfoCard(
                name = place.name,
                distance = place.description, //, should be distance
                onItemClick = {
                    navController.navigate("infoscreen/${place.lat}/${place.long}/${place.id}")
                },
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun HorizontalInfoCard(name: String, distance: String, onItemClick: () -> Unit, modifier: Modifier) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(200.dp)
            .clickable(onClick = onItemClick), //Click to infoscreen
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp

    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.sunset_picture),
                contentDescription = "sunset image placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
            )
            // Box for text displayed from the middle and down, covering the bottom half
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(0.3f)
                    .background(color = md_theme_dark_onSecondary),

                ) {
                //Text for name of place
                Text(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 5.dp),
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = md_theme_light_primaryContainer,
                )
                Text(
                    text = "Distance: 15 m", //distance instead
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(vertical = 4.dp)
                        .padding(start = 15.dp, bottom = 4.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.bodySmall,
                    color = md_theme_light_primaryContainer
                )

            }
        }

    }

}


@Preview
@Composable
fun Test(navController: NavController = rememberNavController()) {
HomeScreen(navController = navController)
}



/**
* Function with alle the content of the homescreen
* This exclude the top- and bottomBar
*/
@Composable
fun ContentHomeScreen(time: String,
temp: String,
sunset: String,
weatherCheck: Boolean
) {
Column (verticalArrangement = Arrangement.Center,
horizontalAlignment = Alignment.CenterHorizontally) {

SunTempAndTime(time, temp)
SunDown(sunset)
Text(text = "Været er bra: $weatherCheck")
//MapBox()
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

@Preview
@Composable
fun TestHorizontalInfoList(navController: NavController = rememberNavController()) {
    HorizontalInfoCard(
        name = "Hei",
        distance = "paa deg",
        onItemClick = { navController.navigate("destination_route") },
        modifier = Modifier
    )
}
