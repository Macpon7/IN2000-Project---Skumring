package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import kotlinx.coroutines.launch
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.SkumringTopAppBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination


object MapListDestination : NavigationDestination {
    override val icon = Icons.Outlined.Place
    override val buttonTitle = R.string.nav_map_button
    override val route = "maplist"
    override val titleRes = R.string.app_name
}

/**
 * Main composable function for displaying the map screen
 */
        @SuppressLint("CoroutineCreationDuringComposition")
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
fun MapListScreen(navController : NavController, mapListViewModel: MapListViewModel = viewModel()) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    // Snackbar:
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // TODO -> must add variables when we make them in viewmodel:
    // Variable to get the error message from viewmodel
    val error = true // Have to change according to the state in viemodel
    val errorMessage = "" // Variable for the errorMessage

    /*
    These belong to searchbar
     */
    //var text by remember { mutableStateOf("") }
    //var active by remember { mutableStateOf(false) }
    Scaffold (topBar = {
        SkumringTopAppBar(
            title = stringResource(id = MapListDestination.titleRes),
            canNavigateBack = false,
            scrollBehavior = scrollBehavior
        )
    },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) {innerPadding ->
        Column (modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            if (error) {

                // Should we show the content or not?
                MapListContent(navController = navController, mapListViewModel = mapListViewModel)

                // launcher for snackbar:
                scope.launch {
                    val result = snackbarHostState
                        .showSnackbar(
                            message = errorMessage,
                            duration = SnackbarDuration.Indefinite,
                            actionLabel = "Refresh",
                        )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {navController.navigate("maplist")}
                        else -> {}
                    }
                }
            }
            else {
                MapListContent(navController = navController, mapListViewModel = mapListViewModel)
            }
        }
    }
}


@Composable
fun MapListContent(navController : NavController, mapListViewModel: MapListViewModel) {
    val mapListUiState: MapListUiState by mapListViewModel.mapListUiState.collectAsState()
    /*
    SearchBar(query = text,
        onQueryChange = {text = it} ,
        onSearch = {active = false },
        active = active,
        onActiveChange =  {active = it}
    ) {
     //TODO legge til søkefelt
    }
     */


    ThemeSwitcher (
        mapTheme = mapListUiState.mapListToggle.stateAsBool,
        size = 65.dp, //Size of the button
        padding = 3.dp,
        onClick = { mapListViewModel.toggleMapListState() }
    )

    if (mapListUiState.mapListToggle == MapListToggleState.MAP) {
        // Column for map view
        MapArea(
            navController = navController,
            mapListUiState = mapListUiState)

    } else {
        // Column for list view
        Column (Modifier.verticalScroll(rememberScrollState())) {
            mapListUiState.places.forEach {place ->
                ListCard(
                    name = place.name,
                    description = place.description,
                    onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                        navController.navigate("infoscreen/${place.lat}/${place.long}/${place.id}")
                    }
                )
            }
        }
    }
}


/**
 * Creates a button with two states, list view and map view
 */
@Composable
fun ListAndMapButton(mapTheme: Boolean, onThemeUpdated: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        ThemeSwitcher(
            mapTheme = mapTheme,
            size = 65.dp, //Size of the button
            padding = 3.dp,
            onClick = onThemeUpdated
        )
    }
}

/**
 * Creates the toggle button. Switches between list view and map view
 */
@Composable
fun ThemeSwitcher(
    mapTheme: Boolean = false,
    size: Dp = 100.dp, //rectangular form
    toggleButtonSize: Dp = 190.dp,
    iconSize: Dp = size / 3,
    padding: Dp = 10.dp,
    borderWidth: Dp = 1.dp,
    parentShape: RoundedCornerShape = RoundedCornerShape(20.dp), //shape of the buttons
    toggleShape: RoundedCornerShape = RoundedCornerShape(20.dp),
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {

    val offset by animateDpAsState(
        targetValue = if (mapTheme) 0.dp else toggleButtonSize,
        animationSpec = animationSpec, label = ""
    )

    //button container
    Box(modifier = Modifier
        .width(size * 6)
        .height(size)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        //toggle animation
        Box(
            modifier = Modifier
                .width(toggleButtonSize) //size of the toggle button
                .height(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        )
        //the icons and text representing list and map views
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(size * 3)
                    .height(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(iconSize)
                        .offset((-30).dp, 0.dp),
                    imageVector = Icons.Default.Add,
                    contentDescription = "Theme Icon",
                    tint = if (mapTheme) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "List",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if(mapTheme) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier
                    .width(size * 3)
                    .height(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier
                        .size(iconSize)
                        .offset((-30).dp, 0.dp),
                    imageVector = Icons.Default.Place,
                    contentDescription = "Theme Icon",
                    tint = if (mapTheme) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer
                )
                Text(
                    text = "Map",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if(mapTheme) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}
/**
 * Placeholder for the map display area
 */
@OptIn(MapboxExperimental::class)
@Composable
fun MapArea(mapListUiState: MapListUiState, navController: NavController) {
    // Can declare point to contain current location of user
    val testPoint = Point.fromLngLat(10.71839307051461, 59.943735106220444)
    var point: Point by remember { mutableStateOf(testPoint) }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .width(500.dp)
            .height(500.dp)
            .padding(6.dp)
            .background(Color.LightGray, RoundedCornerShape((16.dp))),
    ) {

//        MapBoxMap(
//            point = point,
//            modifier = Modifier.fillMaxSize(),
//            context = context
//        )
        MapboxMap(
            Modifier.fillMaxSize(),
            mapInitOptionsFactory = { context ->
                MapInitOptions(
                    context = context,
                    styleUri = Style.OUTDOORS,
                    cameraOptions = CameraOptions.Builder()
                        .center(point)
                        .zoom(10.0)
                        .build()
                )
            }
        ) {
            mapListUiState.pins.forEach { pinfo ->
                val long = pinfo.long.toDouble()
                val lat = pinfo.lat.toDouble()
                point = Point.fromLngLat(long, lat)
                PointAnnotation(
                    point = point,
                    iconImageBitmap = context.getDrawable(R.drawable.location_on)!!.toBitmap(),
                    onClick = {
                        navController.navigate("infoscreen/${pinfo.lat}/${pinfo.long}/${pinfo.id}")
                        Log.d("Home", "Click!")
                        true
                    }
                )
            }
            // Annotation showing custom coordinate should be here
//            PointAnnotation(
//                point = point,
//               iconImageBitmap = context.getDrawable(R.drawable.location_on)!!.toBitmap(),
//                onClick = {
//                   Log.d("Home", "Click!")
//                   true
//                },
//            )
        }
    }
}

/**
 * Cards with information about places
 */
@Composable
fun ListCard(name: String, description: String, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .clickable(onClick = onItemClick) //Click to infoscreen
    ){

        //Box for picture:
        Box(
            modifier = Modifier
                .height(150.dp)
                .padding(6.dp)
                .background(Color.LightGray, RoundedCornerShape((16.dp)))
                .fillMaxWidth(),
        ) {
            Text(
                text = "Image Placeholder",
                modifier = Modifier.align(Alignment.Center)
            )
        }

        //Text for name of place
        Text(
            text = name,
            modifier = Modifier
                .padding(vertical = 2.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        //Text for description. Do we want weather condition in the future?
        Text(
            text = description,
            modifier = Modifier
                .padding(vertical = 2.dp)
                .padding(bottom = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,)
    }
}