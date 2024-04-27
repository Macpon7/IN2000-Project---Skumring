package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.ImageDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.WeatherIconCheck
import java.io.File
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
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaceInfoScreen(
    placeViewModel: PlaceInfoViewModel = viewModel(factory = PlaceInfoViewModel.Factory),
    id: Int,
    navController: NavHostController
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
            SunEventInfoContent(placeUiState)
            //}
        }
    }
}


/**
 * Picture of the place
 */
@SuppressLint("SimpleDateFormat")
@Composable
fun PlaceInfoCard(
    sunEvent: SunEvent,
    placeInfo: PlaceInfo,
    imageDetails: ImageDetails,
    dateString: String,
    timeString: String
) {
    var expanded by remember {mutableStateOf(false)}

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(560.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box( //for the image
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                        )
                    )
            ) {
                /*
                if(placeInfo.sunEvents.isNotEmpty()) {
                    if (placeInfo.id < 16) {
                        Image(
                            painter = painterResource(id = imageDetails.path.toInt()),
                            contentDescription = imageDetails.description
                        )
                    } else {
                        val context = LocalContext.current
                        val imagePath = File(context.filesDir, imageDetails.path)
                        val imagePainter = imagePath.path.toInt()
                        Image(
                            painter = painterResource(imagePainter), contentDescription = null

                        )
                    }
               }

                 */
            }
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSecondary)) {
                        append("imageDetails.description, Placeholder text before the actual text works and we get a place that displays the actual thing we want to display blalbaasdfgldfjs")
                    }
                },
                style = typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(start = 10.dp, bottom = 20.dp, end = 10.dp, top = 5.dp)
            )
            Text(
                text = dateString.uppercase(),
                fontWeight = FontWeight.Bold,
                style = typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary //change text color
            )
            Divider( //for dividing sunset today info from golden hour and blue hour times
                modifier = Modifier.padding(
                    start = 18.dp, end = 18.dp, top = 10.dp, bottom = 15.dp
                ), color = MaterialTheme.colorScheme.onSecondary, thickness = 1.dp
            )
            Icon(
                painter = painterResource(id = R.drawable.sunsetsymbol),
                contentDescription = "Sunset Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(60.dp)
            )
            Text(
                text = timeString, //"${sunEvent.time}",//sunset time //Use timeString instead??
                style = typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 3.dp)
            )
            Text( //text changing based on weather conditions, in different textbox because of change of color
                text = "Sunset conditions: ${sunEvent.conditions.weatherRating}",//" $weatherConditions",
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            )

            Text(
                text = "Temperature at sunset: ${sunEvent.tempAtEvent}",//"$temp °C",
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
            Divider( //for dividing sunset today info from golden hour and blue hour times
                modifier = Modifier.padding(
                    start = 18.dp, end = 18.dp, top = 10.dp, bottom = 15.dp
                ), color = MaterialTheme.colorScheme.onSecondary, thickness = 1.dp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)

            ) {
                Box(
                    modifier = Modifier.size(80.dp)
                ) {
                    WeatherIconCheck(weatherCondition = sunEvent.weatherIcon)
                }

                Box {
                    Text(
                        text = stringResource(R.string.golden_hour),
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 0.dp)
                    )//Golden hour icon and time
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
                        contentDescription = "yellow sun icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(
                            start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                        )

                    )
                    Text(
                        text = "19:09 -20:31", //change this later to $goldenHourTime
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                        )
                    )
                }
                Box {
                    Text(
                        text = stringResource(R.string.blue_hour),
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        textAlign = TextAlign.Center,
                    )//Blue hour icon and time
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.blaasol),
                        contentDescription = "blue sun icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(
                            start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                        )
                    )
                    Text(
                        text = "20:31-21:05", //change this later to $blueHourTime
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.surfaceVariant,
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunEventInfoCard(sunEvent: SunEvent, dateString: String, timeString: String) {

    var expandedState by remember { mutableStateOf(false) }

    //Rotationstate of arrow
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )

    Card(elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300, easing = LinearOutSlowInEasing
                )
            ),
        onClick = { expandedState = !expandedState }

    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Text( //the date
                text = dateString.uppercase(),
                fontWeight = FontWeight.Bold,
                style = typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary //change text color
            )
            Divider( //for dividing the date from the sunset info
                modifier = Modifier.padding(
                    start = 18.dp, end = 18.dp, top = 5.dp, bottom = 15.dp
                ), color = MaterialTheme.colorScheme.onSecondary, thickness = 1.dp
            )
            Icon( //Sunset icon
                painter = painterResource(id = R.drawable.sunsetsymbol),
                contentDescription = "Sunset Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(40.dp)
            )
            Text( //Time of sunset
                text = timeString, //"${sunEvent.time}",
                style = typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 3.dp)
            )
            Text( //text changes based on weather conditions
                text = "Sunset conditions: ${sunEvent.conditions.weatherRating}",
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            )
            Text( //temperature at sunset
                text = "Temperature at sunset: ${sunEvent.tempAtEvent}",//"$temp °C",
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp)
            )
            Box( //Box for the button
                modifier = Modifier.background(MaterialTheme.colorScheme.tertiary)
            ) {
                Divider(
                    color = MaterialTheme.colorScheme.onSecondaryContainer, thickness = 1.dp
                )
                Button(
                    onClick = {
                        expandedState = !expandedState
                    },
                    shape = RectangleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, end = 0.dp)
                ) {
                    Text(
                        text = if (expandedState) "Less details" else stringResource(R.string.home_more_details_button),
                        color = MaterialTheme.colorScheme.onTertiary,
                        style = typography.titleMedium
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Drop-down arrow",
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier
                            .rotate(rotationState)
                            .padding(start = 7.dp)
                    )
                }
            }
            //if button pushed, show the rest of the information
            if (expandedState) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)

                ) {
                    Box(
                        modifier = Modifier.size(60.dp)
                    ) {
                        WeatherIconCheck(weatherCondition = sunEvent.weatherIcon)
                    }
                    Box {
                        Text(
                            text = stringResource(R.string.golden_hour),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 0.dp)
                        )//Golden hour icon and time
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
                            contentDescription = "yellow sun icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(
                                start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )

                        )
                        Text(
                            text = "19:09 -20:31", //change this later to $goldenHourTime
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )
                        )
                    }
                    Box {
                        Text(
                            text = stringResource(R.string.blue_hour),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            textAlign = TextAlign.Center,
                        )//Blue hour icon and time
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.blaasol),
                            contentDescription = "blue sun icon",
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(
                                start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )
                        )
                        Text(
                            text = "20:31-21:05", //change this later to $blueHourTime
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.surfaceVariant,
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
}




/**
 * Function with alle the content of the homescreen
 * This exclude the top- and bottomBar
 */
@Composable
fun ContentInfoScreen(
    placeInfoUiState: PlaceInfoUiState
) {
    Column(
       // modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //Add space between pictures and text
       // Spacer(modifier = Modifier.height(20.dp))

/*
        //Description of the place:
        Column(
            modifier = Modifier
              //  .padding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start,

            ) {
            /*
            Text(
                text = placeInfoUiState.placeInfo.description,
                modifier = Modifier.padding(bottom = 4.dp),
                fontSize = 20.sp
            )
             */

 */

            SunEventInfoContent(placeInfoUiState)
        }
    }
//}

@Composable
fun SunEventInfoContent(placeInfoUiState: PlaceInfoUiState) {
    var dayOffset = 1

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(state = rememberScrollState(), enabled = true)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        // Shows the sunset events for today
        val firstEvent = placeInfoUiState.placeInfo.sunEvents.firstOrNull()
        if (firstEvent != null) {
            SunEventInfoToday(placeInfoUiState)
        }
        Spacer(modifier = Modifier.height(10.dp))

        // Shows the sunset events for tomorrow and the following days
        placeInfoUiState.placeInfo.sunEvents.forEachIndexed { _, _ ->
            SunEventInfoTomorrow(placeInfoUiState, dayOffset)
            dayOffset++
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

/**
 * Displays information about the sunset events for today, date, time and weather conditions
 */
@Composable
fun SunEventInfoToday(placeInfoUiState: PlaceInfoUiState) {

    val placeInfo = placeInfoUiState.placeInfo
    val sunEvents = placeInfo.sunEvents


    if (placeInfo.sunEvents.isNotEmpty()) {
        val sunEvent = sunEvents[0]
        val imageDetails = placeInfo.images.getOrElse(0) { ImageDetails("", "") }
        val time = LocalDateTime.now()


        val dateString =
            "I dag ${time.format(DateTimeFormatter.ofPattern("d'.' MMMM':'", Locale.getDefault()))}"

        val timeString = time.format(DateTimeFormatter.ofPattern("HH':'mm"))

        PlaceInfoCard(sunEvent, placeInfo, imageDetails, dateString, timeString)
    }
}

/**
 * Displays information about the sun events for tomorrow and the following days. Date, time and weather conditions
 */
@Composable
fun SunEventInfoTomorrow(placeInfoUiState: PlaceInfoUiState, dayOffset: Int) {

    val sunEvents = placeInfoUiState.placeInfo.sunEvents
    val index = dayOffset - 1

        if (index < sunEvents.size) {
            val sunEvent = sunEvents[index]

            val date = LocalDateTime.now().plusDays(dayOffset.toLong())
            val dateString = if (dayOffset == 1) {
                // The current date we are formatting is tomorrow
                 "I Morgen ${
                    date.format(
                        DateTimeFormatter.ofPattern(
                            "d'.' MMMM':'", Locale.getDefault()
                        )
                    )
                }"

            } else {
                // The current date we are formatting is after tomorrow
                 date.format(DateTimeFormatter.ofPattern("eeee d'.' MMMM':'", Locale.getDefault()))
            }
            val timeString = sunEvent.time.format(DateTimeFormatter.ofPattern("HH':'mm"))

            SunEventInfoCard(sunEvent, dateString, timeString)
        }
    }


