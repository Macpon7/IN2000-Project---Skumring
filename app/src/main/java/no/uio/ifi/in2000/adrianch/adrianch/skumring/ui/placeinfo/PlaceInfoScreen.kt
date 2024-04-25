package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val TAG = "PlaceInfoScreen"

object PlaceInfoScreenDestination : NavigationDestination {
    override val icon = null
    override val buttonTitle = null
    override val route = "placeinfoscreen/{id}"
    override val titleRes = null
}

@Preview
@Composable
fun PreviewContentInfoScreen() {
    Surface {
        ContentInfoScreen(
            description = "",
            placeInfoUiState = PlaceInfoUiState(
                placeInfo = PlaceInfo(
                    id = 0,
                    name = "Holmenkollen",
                    description = "Et fantastisk fint sted å ta bilde av dine nære og kjære under en solnedgang som ikke kan sammenlignes med noe annet",
                    lat = "",
                    long = "",
                    isFavourite = false,
                    isCustomPlace = false,
                    hasNotification = false,
                    images = emptyList(),
                    sunEvents = listOf(
                        SunEvent(
                            time = LocalDateTime.now(),
                            tempAtEvent = "4.7",
                            weatherIcon = "suncloudy",
                            conditions = WeatherConditions(
                                weatherRating = WeatherConditionsRating.EXCELLENT,
                                cloudConditionLow = CloudConditions.CLEAR,
                                cloudConditionHigh = CloudConditions.CLEAR,
                                cloudConditionMedium = CloudConditions.CLEAR,
                                airCondition = AirConditions.LOW
                            )
                        )
                    )
                )
            ))
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaceInfoScreen(
    placeViewModel: PlaceInfoViewModel = viewModel(factory = PlaceInfoViewModel.Factory),
    id: Int, navController: NavHostController
) {

    val placeUiState: PlaceInfoUiState by placeViewModel.placeInfoUiState.collectAsState()

    LaunchedEffect(Unit) {
        Log.d(TAG, "LaunchedEffect called, loading place with id: $id")
        placeViewModel.loadPlaceInfo(id = id)
    }


    // Check if there is an error, if so show a snackbar:
    if (placeUiState.showSnackbar) {
        LaunchedEffect(placeUiState.snackbarHostState) {
            val result = placeUiState.snackbarHostState.showSnackbar(
                message = placeUiState.errorMessage,
                withDismissAction = true,
                actionLabel = "Refresh",
            )
            // If the snackbar is dismissed, reset the boolean of the showSnackbar-variable
            // The snackbar will reappear is we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {
                    placeViewModel.refresh(id = id)
                }
                // If you click somewhere on the screen
                SnackbarResult.Dismissed -> {
                    placeViewModel.snackbarDismissed()
                }
            }
        }
    }

    Scaffold(topBar = {
        SkumringTopBar(title = placeUiState.placeInfo.name,
            canNavigateBack = true,
            navigateUp = { navController.popBackStack() })
    }, bottomBar = {
        SkumringBottomBar(navController = navController)
    }, snackbarHost = {
        SnackbarHost(hostState = placeUiState.snackbarHostState)
    }) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Padding for topbar
        ) {
            /*if (placeUiState.isLoading) {
                // The content won't load before the content is ready
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {*/
            ContentInfoScreen(
                description = placeUiState.placeInfo.description, placeInfoUiState = placeUiState
            )
            //}
        }
    }
}

/*
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
 */

/**
 * Picture of the place
 */
@Composable
fun PlaceInfoCard() {
    /*
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        */
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(560.dp),
            elevation = CardDefaults.cardElevation(10.dp),
            colors = CardDefaults.cardColors( MaterialTheme.colorScheme.secondary)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                Box( //for the image
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.Gray)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp))
                ) {
                   // Image(painter = painterResource(id = R.drawable.sunset_picture), contentDescription = "sunset default picture")
                }
                /* //if we want name of place
                    Text( //for the Title
                        text = "Holmenkollen",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(start = 10.dp, top = 10.dp)
                    )
                 */
                    Text( //for image description
                        text = "Lorem ipsum is simply dummy text for the printing and typesetting industry. Lorem Impsum bla bla bla bla, legge til noe her sånn at man ser om det funker, det er bra det vet du. Trenger den enda mer? Uff, det her var slitsomt, men snart får vi se om det funker ",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 10.dp, bottom = 20.dp, end = 10.dp, top = 5.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSecondary //change text color
                    )
                Text(
                    text = "Today, april 2.".uppercase(),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondary //change text color
                )
                Divider( //for dividing sunset today info from golden hour and blue hour times
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 15.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    thickness = 1.dp
                )
                Icon(
                    painter = painterResource(id = R.drawable.sunsetsymbol),
                    contentDescription = "Sunset Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.fillMaxWidth()
                        .size(60.dp)
                )
                Text(
                    text = "20:05",//sunsetTime,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 5.dp, top = 3.dp)
                )
                Text( //text changing based on weather conditions, in different textbox because of change of color
                    text = "Sunset conditions: POOR",//" $weatherConditions",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 3.dp)
                )

                Text(
                    text = "Temperature at sunset: 18 C",//"$temp °C",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp)
                )
                Divider( //for dividing sunset today info from golden hour and blue hour times
                    modifier = Modifier.padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 15.dp),
                    color = MaterialTheme.colorScheme.onSecondary,
                    thickness = 1.dp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)

                ) {
                    Icon ( //if icon is null, "show image not found"
                        painterResource(id = R.drawable.rain_icon),
                        contentDescription = "Weather icon cloudy",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(60.dp)

                    )
                    Box {
                        Text(
                            text = stringResource(R.string.golden_hour),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 0.dp)
                        )//Golden hour icon and time
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
                            contentDescription = "yellow sun icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp)

                        )
                        Text(
                            text = "19:09 -20:31", //change this later to $goldenHourTime
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp)
                        )
                    }
                    Box {
                        Text(
                            text = stringResource(R.string.blue_hour),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                           // modifier = Modifier.padding(start = 10.dp)


                        )//Blue hour icon and time
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.blaasol),
                            contentDescription = "blue sun icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp)
                        )
                        Text(
                            text = "20:31-21:05", //change this later to $blueHourTime
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp)
                        )
                    }

                }
                }

            }
        }


//}

/*
/**
 * Picture of the place
 */
@Composable
fun PlacePictureWithContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray, RoundedCornerShape((16.dp))),
    ) {
        Text(
            text = "Place Display Placeholder", modifier = Modifier.align(Alignment.Center)
        )
    }
}
 */

/**
 * Function with alle the content of the homescreen
 * This exclude the top- and bottomBar
 */
@Composable
fun ContentInfoScreen(
    description: String, placeInfoUiState: PlaceInfoUiState
) {
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Picture of the place:
        PlaceInfoCard()

        //Add space between pictures and text
        Spacer(modifier = Modifier.height(20.dp))


        //Description of the place:
        Column(
            modifier = Modifier
                .padding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,

            ) {
            Text(text = description, modifier = Modifier.padding(bottom = 4.dp), fontSize = 20.sp)

            SunEventInfoContent(placeInfoUiState)
        }
    }
}

@Composable
fun SunEventInfoContent(placeInfoUiState: PlaceInfoUiState) {
    var showLongTermForecast by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .verticalScroll(state = rememberScrollState(), enabled = true)
            .fillMaxWidth()
    ) {
        //The accurately forecast sunsets, always show this:
        placeInfoUiState.placeInfo.sunEvents.forEach {
            SunEventInfo(time = it.time, conditions = it.conditions.weatherRating)
        }
    }
}

@Composable
fun SunEventInfo(time: LocalDateTime, conditions: WeatherConditionsRating) {
    val dateString = when (time.dayOfYear) {
        LocalDateTime.now().dayOfYear -> {
            // The current date we are formatting is today
            "I dag ${time.format(DateTimeFormatter.ofPattern("d'.' MMMM':'", Locale.getDefault()))}"
        }

        LocalDateTime.now().plusDays(1).dayOfYear -> {
            // The current date we are formatting is tomorrow
            "I Morgen ${
                time.format(
                    DateTimeFormatter.ofPattern(
                        "d'.' MMMM':'", Locale.getDefault()
                    )
                )
            }"
        }

        else -> {
            // The current date we are formatting is after tomorrow
            time.format(DateTimeFormatter.ofPattern("eeee d'.' MMMM':'", Locale.getDefault()))
        }
    }

    val timeString = time.format(DateTimeFormatter.ofPattern("HH':'mm"))

    val conditionsString = when (conditions) {
        WeatherConditionsRating.POOR -> "Det blir dårlige forhold.."
        WeatherConditionsRating.DECENT -> "Det blir OK forhold"
        WeatherConditionsRating.EXCELLENT -> "Det blir fantastiske forhold!"
    }

    SunEventInfoCard(
        dateString = dateString, timeString = timeString, conditionsString = conditionsString
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunEventInfoCard(dateString: String, timeString: String, conditionsString: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 12.dp),
        onClick = { /* Click listener goes here */ } // TODO Make a button to show more
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Date and time:
            Text(
                text = dateString,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Time for sundown:
            Text(
                text = "Solnedgang: ${timeString}",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Good vs. bad weather:
            Text(
                text = conditionsString, textAlign = TextAlign.Start, fontSize = 14.sp
            )
        }
    }
}