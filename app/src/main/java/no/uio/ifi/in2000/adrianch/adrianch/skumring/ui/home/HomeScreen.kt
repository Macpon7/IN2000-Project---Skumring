package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental

import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.SkumringTopAppBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {//This one is used in the SkumringButtonBar to choose destination
    override val icon = Icons.Outlined.Home //Show home-icon
    override val buttonTitle = R.string.nav_home_button //This is in Int, have to use stringResource to get the String from string.xml
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {

    val homeUiState: HomeUiState by homeViewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior() //var her før
    val temp: String = homeUiState.temp
    val weatherCondition: String = try {
        homeUiState.weatherConditions.text
    } catch (e: Exception){""}
    val sunsetTime = homeUiState.sunsetTime
    val sunsetDate = homeUiState.sunsetDate


    Scaffold(
        topBar = {
            SkumringTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column (modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ContentHomeScreen(sunsetTime, temp, sunsetTime, weatherCondition)
        }
    }
}

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
        MapBox()
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
            color = Color.Black,
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



/**
 * Function with the map inside
 * Should be put inside the contentHomeScreen
 */
@OptIn(MapboxExperimental::class)
@Composable
fun MapBox() {
    // Can declare point to contain current location of user
    val testPoint = Point.fromLngLat(10.71839307051461, 59.943735106220444)
    val point: Point by remember { mutableStateOf(testPoint) }
    // In case of needing to recheck permissions
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .width(500.dp)
            .height(600.dp)
            .padding(6.dp)
            .background(Color.LightGray, RoundedCornerShape((16.dp))),
    ) {
        // Reintroducing placeholder text until I can figure this out
//        MapBoxMap(
//            point = point,
//            modifier = Modifier.fillMaxSize(),
//            context = context
//        )
        Text(
            text = "Map Displayholder",
            modifier = Modifier.align(Alignment.Center)
        )
//        MapboxMap(
//            Modifier.fillMaxSize(),
//            mapInitOptionsFactory = { context ->
//                MapInitOptions(
//                    context = context,
//                    styleUri = Style.OUTDOORS,
//                    cameraOptions = CameraOptions.Builder()
//                        .center(point)
//                        .zoom(10.0)
//                        .build()
//                )
//            }
//        ) {
//            PointAnnotation(
//                point = point,
//                iconImageBitmap = context.getDrawable(R.drawable.location_on)!!.toBitmap(),
//                onClick = {
//                    Log.d("Home", "Click!")
//                    true
//                }
//            )
//        }
    }
}

@Composable
@Preview
fun Test() {
    HomeScreen()
}