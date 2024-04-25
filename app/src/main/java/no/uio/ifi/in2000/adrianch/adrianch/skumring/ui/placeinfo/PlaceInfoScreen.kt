package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val logTag = "PlaceInfoScreen"

object PlaceInfoScreenDestination : NavigationDestination {
    override val icon = null
    override val buttonTitle = null
    override val route = "placeinfoscreen/{id}"
    override val titleRes = null
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

    Scaffold(
        topBar = {
            SkumringTopBar(
                title = placeUiState.placeInfo.name,
                canNavigateBack = true,
                navigateUp = { navController.popBackStack() })
        },
        bottomBar = {
            SkumringBottomBar(navController = navController)
        },
        snackbarHost = {
            SnackbarHost(hostState = placeUiState.snackbarHostState)
        }) {innerPadding ->

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
                description = placeUiState.placeInfo.description,
                placeInfoUiState = placeUiState
                )
            //}
        }
    }
}

/**
 * Picture of the place
 */
@Composable
fun PlacePicture() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray, RoundedCornerShape((16.dp))),
    ) {
        Text(
            text = "Place Display Placeholder",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

/**
 * Function with alle the content of the homescreen
 * This exclude the top- and bottomBar
 */
@Composable
fun ContentInfoScreen(
    description: String,
    placeInfoUiState: PlaceInfoUiState) {
    Column (
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        //Picture of the place:
        PlacePicture()

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
        if (placeInfoUiState.placeInfo.sunEvents.size > 3) {
            placeInfoUiState.placeInfo.sunEvents.subList(0, 3).forEach {
                SunEventInfo(time = it.time, conditions = it.conditions.weatherRating)
            }

            // Dropdown menu for long-term forecast, optional to show:
            Row(
                modifier = Modifier
                    .clickable { showLongTermForecast = !showLongTermForecast }
                    .padding(top = 6.dp)
            ) {
                Text(
                    text = "Langtidsvarsel:",
                    fontSize = 20.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (showLongTermForecast) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle Long Term Forecast",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(36.dp)
                )
            }

            // Check if the arrow-icon is clicked on
            if (showLongTermForecast) {
                placeInfoUiState.placeInfo.sunEvents.subList(3, placeInfoUiState.placeInfo.sunEvents.size).forEach {
                    SunEventInfo(time = it.time, conditions = it.conditions.weatherRating)
                }
            }
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
            "I Morgen ${time.format(DateTimeFormatter.ofPattern("d'.' MMMM':'", Locale.getDefault()))}"
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
        dateString = dateString,
        timeString = timeString,
        conditionsString = conditionsString
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SunEventInfoCard(dateString: String, timeString: String, conditionsString: String) {
    Card(
        modifier = Modifier
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
                text = conditionsString,
                textAlign = TextAlign.Start,
                fontSize = 14.sp
            )
        }
    }
}