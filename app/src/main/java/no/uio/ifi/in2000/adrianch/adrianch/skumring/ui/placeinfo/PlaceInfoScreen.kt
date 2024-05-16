package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.AppDatabase
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.MeansOfTransportation
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.directions.TravelDurationDistance
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.WeatherIconCheck
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.WeatherIconPopUp
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

/**
 * The screen with top bar and bottom bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaceInfoScreen(
    placeInfoViewModel: PlaceInfoViewModel = viewModel(factory = PlaceInfoViewModel.Factory),
    id: Int,
    navController: NavHostController
) {

    val placeUiState: PlaceInfoUiState by placeInfoViewModel.placeInfoUiState.collectAsState()

    //for snackbar
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.d(TAG, "LaunchedEffect called, loading place with id: $id")
        placeInfoViewModel.snackbarDismissed()
        placeInfoViewModel.loadPlaceInfo(id = id)
    }

    // Check if there is an error, if so show a snackbar:
    if (placeUiState.showSnackbar) {
        LaunchedEffect(placeUiState.snackbarHostState) {
            val result = placeUiState.snackbarHostState.showSnackbar(
                message = placeUiState.errorMessage,
                withDismissAction = true,
                actionLabel = context.getString(R.string.refresh),
            )
            // If the snackbar is dismissed, reset the boolean of the showSnackbar-variable
            // The snackbar will reappear is we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {
                    placeInfoViewModel.refresh(id = id)
                }
                // If you click somewhere on the screen
                SnackbarResult.Dismissed -> {
                    placeInfoViewModel.snackbarDismissed()
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
            PlaceInfoContent(placeUiState, placeInfoViewModel::toggleFavourite)
        }
    }
}

/**
 * Function with alle the content of the homescreen
 * This exclude the top- and bottomBar
 */
@Composable
fun PlaceInfoContent(
    placeInfoUiState: PlaceInfoUiState, toggleFavourite: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(state = rememberScrollState(), enabled = true)
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        if (placeInfoUiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(100.dp),
                color = MaterialTheme.colorScheme.onPrimary)
        } else {
            // Show information about tonight's sunset
            TodayInfoCard(
                placeInfo = placeInfoUiState.placeInfo,
                mapTimeDistance = placeInfoUiState.mapTimeDistance,
                onFavouriteClick = toggleFavourite
            )

            //space between the cards
            Spacer(modifier = Modifier.height(12.dp))

            // Shows the sunsets for tomorrow and the following day
            placeInfoUiState.placeInfo.sunEvents.subList(1, 3).forEach {
                SunEventInfoCard(it)

                //space between the cards
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

/**
 * Shows information about the sunset today, as well as picture.
 * You can also favourite the place
 */
@SuppressLint("SimpleDateFormat")
@Composable
fun TodayInfoCard(
    placeInfo: PlaceInfo,
    mapTimeDistance: Map<MeansOfTransportation, TravelDurationDistance>,
    onFavouriteClick: () -> Unit
) {
    //the current date we are formatting to is today
    val dateString = "${stringResource(R.string.today)} ${
        placeInfo.sunEvents[0].time.format(
            DateTimeFormatter.ofPattern(
                "d'.' MMMM':'", Locale.getDefault()
            )
        )
    }"
    val nullTime = LocalDateTime.of(2000, 1, 1, 0, 0)
    val formatter = DateTimeFormatter.ofPattern("HH':'mm")

    //for the clickable text
    var expanded by remember { mutableStateOf(false) }

    //for remembering if placeinfo is favourite or not
    var isFavourite by remember { mutableStateOf(placeInfo.isFavourite) }

    //popup for displaying more information about weather conditions
    var showPopUp by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //PlaceInfo image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.Gray)
            ) {
                //if place is custom, read image from filesDir, otherwise use assets folder
                if (placeInfo.isCustomPlace) {
                    //this is for fetching/getting images that are uploaded into internal storage
                    val context = LocalContext.current
                    val imageFile = File(context.filesDir, placeInfo.images[0].path)
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
                        model = "file:///android_asset/presetImages/${placeInfo.images[0].path}",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }

                //IconButton for choosing place as favourite
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onFavouriteClick,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 15.dp, end = 15.dp)
                    ) {
                        //if favourite, show filled heart
                        if (placeInfo.isFavourite) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                modifier = Modifier.size(40.dp),
                                contentDescription = stringResource(R.string.favourite_icon),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            //if not favourite, show heart with border
                        } else {
                            Icon(
                                imageVector = Icons.Filled.FavoriteBorder,
                                modifier = Modifier.size(40.dp),
                                contentDescription = stringResource(R.string.favourite_icon),
                                tint = MaterialTheme.colorScheme.secondary

                            )
                        }
                    }
                }
            }
            //Clickable text. If description is over two lines, show only the two lines. If clicked,
            //shows the rest of the text
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(MaterialTheme.colorScheme.onSurfaceVariant)) {
                        append(
                            stringResource(id = R.string.picture_taken),
                            ": ",
                            placeInfo.images[0].timeStamp.format(DateTimeFormatter.ISO_LOCAL_DATE),
                            ". ",
                            placeInfo.description
                        )
                    }
                },
                style = typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(start = 10.dp, bottom = 20.dp, end = 10.dp, top = 5.dp)
            )
            Text(
                stringResource(R.string.distance_from_location),
                modifier = Modifier
                    .padding(start = 15.dp, top = 0.dp, bottom = 15.dp)
                    .align(Alignment.CenterHorizontally),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outlineVariant,
                style = typography.titleMedium
            )
            //for showing distance to the place by walking, biking and driving
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 30.dp, end = 30.dp)
            ) {
                // WALKING
                Column {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.walk),
                            contentDescription = stringResource(R.string.walk_icon),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = stringResource(R.string.walk),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Text(
                        text = mapTimeDistance[MeansOfTransportation.WALKING]?.distance +
                                " " + stringResource(R.string.distance_kilometers),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            top = 5.dp,
                        )
                    )
                    Text(
                        text = mapTimeDistance[MeansOfTransportation.WALKING]?.durationHours +
                                stringResource(id = R.string.distance_hour) + " " +
                                mapTimeDistance[MeansOfTransportation.WALKING]?.durationMinutes +
                                stringResource(id = R.string.distance_minutes),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
                //BIKING
                Column {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.bike),
                            contentDescription = stringResource(R.string.bike_icon),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = stringResource(R.string.bike),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Text(
                        text = mapTimeDistance[MeansOfTransportation.BIKING]?.distance +
                                " " + stringResource(R.string.distance_kilometers),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            top = 5.dp
                        )
                    )
                    Text(
                        text = mapTimeDistance[MeansOfTransportation.BIKING]?.durationHours +
                                stringResource(id = R.string.distance_hour) +
                                " " +
                                mapTimeDistance[MeansOfTransportation.BIKING]?.durationMinutes +
                                stringResource(id = R.string.distance_minutes),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,

                        )
                }
                // DRIVING
                Column {
                    Row {

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.drive),
                            contentDescription = stringResource(R.string.drive_icon),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,

                            )
                        Text(
                            text = stringResource(R.string.drive),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Text(
                        text = mapTimeDistance[MeansOfTransportation.DRIVING]?.distance +
                                " " + stringResource(R.string.distance_kilometers),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            top = 5.dp
                        )
                    )
                    Text(
                        text = mapTimeDistance[MeansOfTransportation.DRIVING]?.durationHours +
                                stringResource(id = R.string.distance_hour) +
                                " " +
                                mapTimeDistance[MeansOfTransportation.DRIVING]?.durationMinutes +
                                stringResource(id = R.string.distance_minutes),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 17.dp) //start = 18.dp, end = 18.dp
                        ,
                 color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp
            )

            //For showing todays date
            Text(
                text = dateString.uppercase(),
                fontWeight = FontWeight.Bold,
                style = typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            //Sunset Icon
            Icon(
                painter = painterResource(id = R.drawable.sunsetsymbol),
                contentDescription = stringResource(R.string.homescreen_icon_sunset),
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(60.dp)
            )
            //Time of sunset
            Text(
                text = placeInfo.sunEvents[0].time.format(formatter),
                style = typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, top = 3.dp)
            )
            Row(  //For displaying weather conditions and information popup
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()

            ) {
                //Conditions at sunset
                Text(
                    text = stringResource(R.string.weather_condition) + " ",
                    style = typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    //text changing based on weather conditions, in different textbox because of change of color
                    text = stringResource(id = placeInfo.sunEvents[0].conditions.weatherRating.stringResourceId),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                )
                //Clickable icon for showing more info about the weather conditions
                Icon(Icons.Default.Info,
                    contentDescription = stringResource(R.string.information_icon),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .clickable { showPopUp = true }
                        .size(30.dp)
                        .padding(start = 5.dp, bottom = 10.dp))
            }
            //Temperature at sunset
            Text(
                text = stringResource(R.string.temp_at_sunset) + ": ${placeInfo.sunEvents[0].tempAtEvent} °C",
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 5.dp)
            )
            Divider(
                modifier = Modifier.padding(
                    start = 27.dp, end = 27.dp, top = 10.dp, bottom = 15.dp
                ), color = MaterialTheme.colorScheme.onSurfaceVariant, thickness = 1.dp
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, bottom = 10.dp)
            ) {
                //Shows the weathericon that suits the weatherconditions today
                Box(
                    modifier = Modifier.size(80.dp)
                ) {
                    WeatherIconCheck(
                        weatherCondition = placeInfo.sunEvents[0].weatherIcon,
                        weather = placeInfo.sunEvents[0].conditions.weatherRating
                    )
                }
                //Box for golden hour icon and time
                Box {
                    Text(
                        text = stringResource(R.string.golden_hour),
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 0.dp)
                    )
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
                        contentDescription = stringResource(R.string.homescreen_icon_yellow_sun),
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(
                            start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                        )

                    )
                    Text(
                        text = if (placeInfo.sunEvents[0].goldenHourTime == nullTime) {
                            "--N/A--"
                        } else {
                            "${placeInfo.sunEvents[0].goldenHourTime.format(formatter)} " +
                                    "- ${placeInfo.sunEvents[0].time.format(formatter)}"
                        },
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                        )
                    )
                }
                //box for blue hour icon and time
                Box {
                    Text(
                        text = stringResource(R.string.blue_hour),
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )//Blue hour icon and time
                    Icon(
                        imageVector = if (isSystemInDarkTheme()) {
                            ImageVector.vectorResource(id = R.drawable.bluesundarkmode)
                        } else {
                            ImageVector.vectorResource(id = R.drawable.bluesunlightmode)
                        },
                        contentDescription = stringResource(R.string.homescreen_icon_blue_sun),
                        tint = Color.Unspecified,
                        modifier = Modifier.padding(
                            start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                        )
                    )
                    Text(
                        text = if (placeInfo.sunEvents[0].blueHourTime == nullTime) {
                            "--N/A--"
                        } else {
                            "${placeInfo.sunEvents[0].time.format(formatter)} " +
                                    "- ${placeInfo.sunEvents[0].blueHourTime.format(formatter)}"
                        },
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                        )
                    )
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
}

/**
 * Displays information about the sun events for tomorrow and the following days.
 * Date, time and weather conditions
 */
@Composable
fun SunEventInfoCard(
    sunEvent: SunEvent
) {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH':'mm")
    val nullTime: LocalDateTime = LocalDateTime.of(2000, 1, 1, 0, 0)

    // Code for formatting the date is here, all other string formatting happens inside each Text component
    val dateString = if (sunEvent.time.dayOfYear == LocalDateTime.now().plusDays(1).dayOfYear) {
        // The current date we are formatting is tomorrow
        "${stringResource(id = R.string.tomorrow)} " +
                sunEvent.time.format(DateTimeFormatter.ofPattern(
                    "d'.' MMMM':'", Locale.getDefault())
                )
    } else {
        // The current date we are formatting is after tomorrow
        sunEvent.time.format(DateTimeFormatter.ofPattern("eeee d'.' MMMM':'", Locale.getDefault()))
    }


    //state for remembering if button is pushed or not
    var expandedState by remember { mutableStateOf(false) }

    //Rotationstate of arrow
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )

    Card(elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300, easing = LinearOutSlowInEasing
                )
            )

    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            //for showing the date
            Text(
                text = dateString.uppercase(),
                fontWeight = FontWeight.Bold,
                style = typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 15.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            //Sunset icon
            Icon(
                painter = painterResource(id = R.drawable.sunsetsymbol),
                contentDescription = stringResource(R.string.homescreen_icon_sunset),
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(40.dp)
            )
            //Time of sunset
            Text(
                text = sunEvent.time.format(formatter),
                style = typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 3.dp)
            )
            //text changes based on weather conditions
            Text(
                text = stringResource(R.string.weather_condition) + " " + stringResource(id = sunEvent.conditions.weatherRating.stringResourceId),
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            )
            Text( //temperature at sunset
                text = stringResource(R.string.temp_at_sunset) + ": ${sunEvent.tempAtEvent}°C",
                style = typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            //Box for "show less"/"show more" button
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Button(
                    onClick = {
                        expandedState = !expandedState
                    },
                    shape = RectangleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, end = 0.dp)
                ) {
                    Row (
                        modifier = Modifier.width(130.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (expandedState) stringResource(R.string.placeInfo_less_details_button) else stringResource(
                                R.string.placeInfo_more_details_button
                            ),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            style = typography.titleMedium
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.dropdown_arrow_icon),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier
                                .rotate(rotationState)
                                .padding(start = 7.dp)
                        )
                    }
                }
            }
            //if button clicked, show the rest of the information
            if (expandedState) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp, bottom = 10.dp, top = 10.dp)

                ) {
                    //Weatherconditions icon for how the weather is at the date
                    Box(
                        modifier = Modifier.size(60.dp)
                    ) {
                        WeatherIconCheck(
                            weatherCondition = sunEvent.weatherIcon,
                            weather = sunEvent.conditions.weatherRating
                        )
                    }
                    //golden hour text, icon and time
                    Box {
                        Text(
                            text = stringResource(R.string.golden_hour),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 0.dp)
                        )
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.gulsol),
                            contentDescription = stringResource(R.string.homescreen_icon_yellow_sun),
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(
                                start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )
                        )
                        Text(
                            text = if (sunEvent.goldenHourTime == nullTime) {
                                "--N/A--"
                            } else {
                                "${sunEvent.goldenHourTime.format(formatter)} - ${sunEvent.time.format(formatter)}"
                            },
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(
                                start = 25.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )
                        )
                    }
                    //blue hour text, icon and time
                    Box {
                        Text(
                            text = stringResource(R.string.blue_hour),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )//Blue hour icon and time
                        Icon(
                            imageVector = if (isSystemInDarkTheme()) {
                                ImageVector.vectorResource(id = R.drawable.bluesundarkmode)
                            } else {
                                ImageVector.vectorResource(id = R.drawable.bluesunlightmode)
                            }
                            ,
                            contentDescription = stringResource(R.string.homescreen_icon_blue_sun),
                            tint = Color.Unspecified,
                            modifier = Modifier.padding(
                                start = 0.dp, bottom = 22.dp, top = 22.dp, end = 22.dp
                            )
                        )
                        Text(
                            text = if (sunEvent.blueHourTime == nullTime) {
                                "--N/A--"
                            } else {
                                "${sunEvent.time.format(formatter)} - ${sunEvent.blueHourTime.format(formatter)}"
                            },
                            style = typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
 * Preview to show PlaceInfoScreen cards
 */
@Preview
@Composable
fun PreviewSunEventInfoScreen() {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context = context)
    val imageDao = database.imageDao()
    val forecastDao = database.forecastDao()
    val placeInfoDao = database.placeInfoDao()
    val placeRepository = PlaceRepositoryImpl(
        placeInfoDao = placeInfoDao, forecastDao = forecastDao, imageDao = imageDao, context = context
    )
    Surface {
        PlaceInfoContent(
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
                                airCondition = AirConditions.LOW,
                            ),
                            blueHourTime = LocalDateTime.now(),
                            goldenHourTime = LocalDateTime.now()
                        )
                    )
                )
            ),
            toggleFavourite = {}
        )
    }
}
