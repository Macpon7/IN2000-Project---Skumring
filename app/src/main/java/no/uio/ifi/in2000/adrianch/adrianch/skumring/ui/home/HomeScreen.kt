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
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.ListCard
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListUiState
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListViewModel
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.md_theme_dark_background
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.md_theme_dark_onPrimary
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.md_theme_dark_tertiaryContainer
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.md_theme_light_primary
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.md_theme_light_primaryContainer
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.sunSetColor
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.sunSetColor2

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

    // var timeUiState: String = homeUiState.time
    var time: String = homeUiState.time
    var temp: String = homeUiState.temp
    var sunset: String = homeUiState.sunset
    var weatherCheck: Boolean = homeUiState.weatherCheck
    var weatherMessage: String = homeUiState.weatherMessage

    Scaffold(
        topBar = {
            SkumringTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SunsetInfoCard()
                HorizontalInfoListContent(
                    mapListUiState = mapListUiState,
                    navController = navController
                )
            }
        }
    }



@Composable
fun HorizontalInfoListContent(mapListUiState: MapListUiState, navController: NavController) {
        mapListUiState.places.forEach { place ->
            HorizontalInfoList(
                name = place.name,
                description = place.description,
                onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                    navController.navigate("infoscreen/${place.lat}/${place.long}/${place.id}")
                }
            )
        }
    }

@Preview
@Composable
fun TestHorizontalInfoList(navController: NavController = rememberNavController()) {
    HorizontalInfoList(name = "Hei", description = "paa deg"
    ) { navController.navigate("destination_route") }
}



@Composable
fun HorizontalInfoList(name: String, description: String, onItemClick: () -> Unit) {

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
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

                    //.align(Alignment.TopCenter)
                )
                // Box for text displayed from the middle and down, covering the bottom half
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        // .align(Alignment.BottomCenter)
                        .fillMaxHeight(0.5f)
                        .padding(12.dp)
                        .background(color = md_theme_light_primaryContainer),
                    contentAlignment = Alignment.Center

                ) {

                    //Text for name of place
                    Text(
                        text = name,
                        modifier = Modifier
                            .paddingFromBaseline(top = 24.dp, bottom = 8.dp),
                        // .padding(vertical = 2.dp)
                        // .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_primaryContainer,
                        fontSize = 20.sp
                    )
                    // Text for description. Do we want weather condition in the future?
                    Text(
                        text = description,
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .padding(bottom = 4.dp)
                            .fillMaxWidth()
                            .padding(start = 15.dp),
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Bold,
                        color = md_theme_light_primaryContainer
                    )

                }
            }

        }
    }
}




@Preview
@Composable
fun SunsetInfoCard() {
Card(
backgroundColor = Color.Transparent, //removing existing backgroundcolor
modifier = Modifier
.padding(16.dp)
.fillMaxWidth()
.background( //adding desired backgroundcolor
brush = Brush.verticalGradient(
colors = listOf(
    md_theme_dark_background,
    md_theme_dark_onPrimary,
    md_theme_dark_tertiaryContainer,
    md_theme_dark_onPrimary
)
)
),
shape = RoundedCornerShape(15.dp)

) {
Column(
verticalArrangement = Arrangement.Center,
horizontalAlignment = Alignment.CenterHorizontally
) {
Text(
text = "Sunset today",
color = md_theme_light_primaryContainer,
style = MaterialTheme.typography.displaySmall,
)
Icon(
painter = painterResource(id = R.drawable.sunsetsymbol),
contentDescription = "Sunset Icon",
modifier = Modifier.size(120.dp),
tint = Color.Unspecified
)
Text(
text = "20:05",
style = MaterialTheme.typography.bodyLarge,
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
style = MaterialTheme.typography.bodyMedium,
fontWeight = FontWeight.Bold,
color = md_theme_light_primaryContainer,
textAlign = TextAlign.Center,

)
Text(
text = "Poor ", //change this later
style = MaterialTheme.typography.bodyMedium,
fontWeight = FontWeight.Bold,
color = md_theme_light_primary,
textAlign = TextAlign.Center,
)
}
Column(
verticalArrangement = Arrangement.Center,
horizontalAlignment = Alignment.CenterHorizontally
) {

Icon(
imageVector = ImageVector.vectorResource(id = R.drawable.weathersymbolcloudy),
contentDescription = "Weather icon cloudy",
tint = Color.Unspecified
)
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
    .padding(start = 40.dp, end = 40.dp)

) {
Box {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.blaasol),
        contentDescription = "Weather icon cloudy",
        tint = Color.Unspecified,
        modifier = Modifier.padding(end = 10.dp)

    )
    Text(
        text = "20:31 -21:05 ", //change this later
        style = MaterialTheme.typography.bodyMedium,
        color = md_theme_light_primaryContainer,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(start = 20.dp)


    )
}
Box {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
        contentDescription = "Weather icon cloudy",
        tint = Color.Unspecified,
    )
    Text(
        text = "19:09-20:31", //change this later
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
modifier = Modifier
    .fillMaxWidth()
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

//horizontalArrangement
//column, maxwidth
//
