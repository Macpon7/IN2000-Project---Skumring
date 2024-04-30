package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.AppDatabase
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepositoryImpl
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
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.WeatherIconPopUp
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
            /*if (placeUiState.isLoading) {
                // The content won't load before the content is ready
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {*/
            SunEventInfoContent(placeUiState, placeInfoViewModel)
            //}
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
    sunEvent: SunEvent,
    placeInfo: PlaceInfo,
    imageDetails: ImageDetails,
    dateString: String,
    timeString: String,
    placeInfoViewModel: PlaceInfoViewModel
) {

    val textColor: Color = MaterialTheme.colorScheme.inverseOnSurface
    val cardColor: Color = MaterialTheme.colorScheme.inversePrimary
    val heartIconColor: Color = MaterialTheme.colorScheme.onPrimary
    val dividerColor: Color = MaterialTheme.colorScheme.surface

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
        colors = CardDefaults.cardColors(cardColor)
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
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp, topEnd = 20.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                        )
                    )
            ) {
                //if placeInfo.id is smaller than 16, show image from imageDetails.path.
                //If placeInfo.id is bigger than 16, load image from file path
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
                //IconButton for choosing place as favourite
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            //if isFavourite = true, add favourite
                            if (!isFavourite) {
                                placeInfoViewModel.addFavourite(placeInfo.id)
                                //else remove favourite
                            } else {
                                placeInfoViewModel.removeFavourite(placeInfo.id)
                            }
                            isFavourite = !isFavourite
                        },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 15.dp, end = 15.dp)
                    ) {
                        //if favourite, show filled heart
                        if (isFavourite) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                modifier = Modifier.size(40.dp),
                                contentDescription = "",
                                tint = heartIconColor
                            )
                            //if not favourite, show heart with border
                        } else {
                            Icon(
                                imageVector = Icons.Filled.FavoriteBorder,
                                modifier = Modifier.size(40.dp),
                                contentDescription = "",
                                tint = heartIconColor

                            )
                        }
                    }
                }
            }
            //Clickable text. If description is over two lines, show only the two lines. If clicked,
            //shows the rest of the text
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textColor)) {
                        append(
                            imageDetails.description,
                            "Placeholder text before the actual text works and we get a place that displays the actual thing we want to display blalbaasdfgldfjs"
                        )
                    }
                },
                style = typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                onClick = { expanded = !expanded },
                modifier = Modifier.padding(start = 10.dp, bottom = 25.dp, end = 10.dp, top = 5.dp)
            )


            //Row for different ways to get to the place
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {

                Column {
                    Row {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.walk),
                            contentDescription = "walk icon",
                            tint = Color.Unspecified,
                        )
                        Text(
                            text = stringResource(R.string.walk),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Text(
                        text = "150 meter", //TODO //change this later to stringformat
                        style = typography.bodyMedium,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            top = 5.dp,
                        )
                    )
                    Text(
                        text = "40 min", //TODO //change this later to stringformat
                        style = typography.bodyMedium,
                        color = textColor,
                        textAlign = TextAlign.Center,
                    )
                }

                Column {
                    Row {

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.bike),
                            contentDescription = "bike icon",
                            tint = Color.Unspecified,
                        )
                        Text(
                            text = stringResource(R.string.bike),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp)
                        )//Blue hour icon and time
                    }
                    Text(
                        text = "150 meter", //TODO //change this later to stringformat
                        style = typography.bodyMedium,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            top = 5.dp
                        )
                    )
                    Text(
                        text = "20 min", //TODO //change this later to stringformat
                        style = typography.bodyMedium,
                        color = textColor,
                        textAlign = TextAlign.Center,

                        )
                }

                Column {
                    Row {

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.drive),
                            contentDescription = "drive icon",
                            tint = Color.Unspecified,

                            )
                        Text(
                            text = stringResource(R.string.drive),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Text(
                        text = "150 km", //TODO //change this later to stringformat
                        style = typography.bodyMedium,
                        color = textColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            top = 5.dp
                        )
                    )
                    Text(
                        text = "10 min", //TODO //change this later to stringformat
                        style = typography.bodyMedium,
                        color = textColor,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            Divider(
                modifier = Modifier.padding(
                    start = 18.dp, end = 18.dp, top = 10.dp, bottom = 15.dp
                ), color = dividerColor, thickness = 1.dp
            )

            //For showing todays date
            Text(
                text = dateString.uppercase(),
                fontWeight = FontWeight.Bold,
                style = typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),
                textAlign = TextAlign.Center,
                color = textColor
            )
            //Sunset Icon
            Icon(
                painter = painterResource(id = R.drawable.sunsetsymbol),
                contentDescription = "Sunset Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(60.dp)
            )
            //Time of sunset
            Text(
                text = timeString,
                style = typography.headlineSmall,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 3.dp)
            )
            Row(  //For displaying weather conditions and information popup
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()

            ) {
                //Conditions at sunset
                Text(
                    text = stringResource(R.string.weather_condition),
                    style = typography.bodyMedium,
                    color = textColor,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    //text changing based on weather conditions, in different textbox because of change of color
                    text = "${sunEvent.conditions.weatherRating}", //TODO fix so this is also in Norwegian
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                )
                //Clickable icon for showing more info about the weather conditions
                Icon(Icons.Default.Info,
                    contentDescription = "Info",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .clickable { showPopUp = true }
                        .size(30.dp)
                        .padding(start = 5.dp, bottom = 10.dp))
            }
            //Temperature at sunset
            Text(
                text = stringResource(R.string.temp_at_sunset) + ": ${sunEvent.tempAtEvent} °C",
                style = typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 5.dp)
            )
            Divider(
                modifier = Modifier.padding(
                    start = 22.dp, end = 22.dp, top = 10.dp, bottom = 15.dp
                ), color = MaterialTheme.colorScheme.onSecondary, thickness = 1.dp
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
                    WeatherIconCheck(weatherCondition = sunEvent.weatherIcon)
                }
                //Box for golden hour icon and time
                Box {
                    Text(
                        text = stringResource(R.string.golden_hour),
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = textColor,
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
                        text = "19:09 -20:31", //TODO //change this later to $goldenHourTime
                        style = typography.bodyMedium,
                        color = textColor,
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
                        color = textColor,
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
                        text = "20:31-21:05", //TODO //change this later to $blueHourTime
                        style = typography.bodyMedium,
                        color = textColor,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunEventInfoCard(
    sunEvent: SunEvent, dateString: String, timeString: String
) {

    //Colors for the card
    val textColor: Color = MaterialTheme.colorScheme.inverseOnSurface
    val cardColor: Color = MaterialTheme.colorScheme.inversePrimary
    val buttonColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
    val buttonTextColor: Color = MaterialTheme.colorScheme.primaryContainer
    val dividerColor: Color = MaterialTheme.colorScheme.surface

    //state for remembering if button is pushed or not
    var expandedState by remember { mutableStateOf(false) }

    //Rotationstate of arrow
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )

    Card(elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(cardColor),
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

            //for showing the date
            Text(
                text = dateString.uppercase(),
                fontWeight = FontWeight.Bold,
                style = typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                textAlign = TextAlign.Center,
                color = textColor
            )
            Divider( //for dividing the date from the sunset info
                modifier = Modifier.padding(
                    start = 18.dp, end = 18.dp, top = 5.dp, bottom = 15.dp
                ), color = MaterialTheme.colorScheme.onSecondary, thickness = 1.dp
            )
            //Sunset icon
            Icon(
                painter = painterResource(id = R.drawable.sunsetsymbol),
                contentDescription = "Sunset Icon",
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(40.dp)
            )
            //Time of sunset
            Text(
                text = timeString,
                style = typography.headlineSmall,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp, top = 3.dp)
            )
            //text changes based on weather conditions
            Text(
                text = stringResource(R.string.weather_condition) + ": ${sunEvent.conditions.weatherRating}",
                style = typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 3.dp)
            )
            Text( //temperature at sunset
                text = stringResource(R.string.temp_at_sunset) + ": ${sunEvent.tempAtEvent}°C",
                style = typography.bodyMedium,
                color = textColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            //Box for "show less"/"show more" button
            Box(
                modifier = Modifier.background(buttonColor)
            ) {
                Divider(
                    color = dividerColor, thickness = 1.dp
                )
                Button(
                    onClick = {
                        expandedState = !expandedState
                    },
                    shape = RectangleShape,
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 0.dp, end = 0.dp)
                ) {
                    Text(
                        text = if (expandedState) stringResource(R.string.placeInfo_less_details_button) else stringResource(
                            R.string.placeInfo_more_details_button
                        ), color = buttonTextColor, style = typography.titleMedium
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Drop-down arrow",
                        tint = buttonTextColor,
                        modifier = Modifier
                            .rotate(rotationState)
                            .padding(start = 7.dp)
                    )
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
                        WeatherIconCheck(weatherCondition = sunEvent.weatherIcon)
                    }
                    //golden hour text, icon and time
                    Box {
                        Text(
                            text = stringResource(R.string.golden_hour),
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
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
                            text = "19:09 -20:31", //TODO //change this later to $goldenHourTime
                            style = typography.bodyMedium,
                            color = textColor,
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
                            color = textColor,
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
                            text = "20:31-21:05", //TODO //change this later to $blueHourTime
                            style = typography.bodyMedium,
                            color = textColor,
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
fun SunEventInfoContent(
    placeInfoUiState: PlaceInfoUiState, placeInfoViewModel: PlaceInfoViewModel
) {

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
            SunEventInfoToday(placeInfoUiState, placeInfoViewModel)
        }

        //space between the cards
        Spacer(modifier = Modifier.height(10.dp))

        // Shows the sunset events for tomorrow and the following days
        placeInfoUiState.placeInfo.sunEvents.forEachIndexed { _, _ ->
            SunEventInfoTomorrow(placeInfoUiState)


        }
    }
}

/**
 * Displays information about the sunset events for today, date, time and weather conditions
 */
@Composable
fun SunEventInfoToday(placeInfoUiState: PlaceInfoUiState, placeInfoViewModel: PlaceInfoViewModel) {

    val placeInfo = placeInfoUiState.placeInfo
    val sunEvents = placeInfo.sunEvents

    if (placeInfo.sunEvents.isNotEmpty()) {
        val sunEvent = sunEvents[0]
        val imageDetails = placeInfo.images.getOrElse(0) { ImageDetails("", "") }

        //the current date we are formatting to is today
        val dateString = "${stringResource(R.string.today)} ${
            sunEvent.time.format(
                DateTimeFormatter.ofPattern(
                    "d'.' MMMM':'", Locale.getDefault()
                )
            )
        }"

        val timeString = sunEvent.time.format(DateTimeFormatter.ofPattern("HH':'mm"))

        TodayInfoCard(
            sunEvent, placeInfo, imageDetails, dateString, timeString, placeInfoViewModel
        )
    }
}


/**
 * Displays information about the sun events for tomorrow and the following days.
 * Date, time and weather conditions
 */
@Composable
fun SunEventInfoTomorrow(placeInfoUiState: PlaceInfoUiState) {

    val sunEvents = placeInfoUiState.placeInfo.sunEvents
    var index = 1

    while (index < sunEvents.size) {
        val sunEvent = sunEvents[index]
        val date = LocalDateTime.now().plusDays(index.toLong())

        val dateString = if (index == 1) {
            // The current date we are formatting is tomorrow
            "${(stringResource(R.string.tomorrow))}  ${
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
        index++

        //time of day for sunset
        val timeString = sunEvent.time.format(DateTimeFormatter.ofPattern("HH':'mm"))

        SunEventInfoCard(sunEvent, dateString, timeString)

        //Spacer between cards
        Spacer(modifier = Modifier.height(10.dp))
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
        placeInfoDao = placeInfoDao, forecastDao = forecastDao, imageDao = imageDao
    )
    Surface {
        SunEventInfoContent(
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
            ), placeInfoViewModel = PlaceInfoViewModel(
                context = context, placeRepository = placeRepository
            )
        )
    }
}
