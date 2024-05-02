package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

import android.graphics.Color
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.gson.JsonPrimitive
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.ListCard
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar

private const val logTag = "MapListScreen"

object MapListDestination : NavigationDestination {
    override val icon = Icons.Outlined.Place
    override val buttonTitle = R.string.nav_map_button
    override val route = "maplist"
    override val titleRes = R.string.app_name
}

/**
 * Main composable function for displaying the map screen
 */
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
fun MapListScreen(navController : NavHostController, mapListViewModel: MapListViewModel = viewModel(factory = MapListViewModel.Factory)) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val mapListUiState: MapListUiState by mapListViewModel.mapListUiState.collectAsState()

    //Variable for using strings in not-composable
    val context = LocalContext.current

    // Load all places every time the user navigates to this screen
    LaunchedEffect(Unit) {
        mapListViewModel.loadPlaces()
        mapListViewModel.updateUserLocation()
    }

    // Check if there is an error, if so show a snackbar:
    if (mapListUiState.showSnackbar) {
        LaunchedEffect(mapListUiState.snackbarHostState) {
            val result = mapListUiState.snackbarHostState.showSnackbar(
                message = mapListUiState.errorMessage,
                withDismissAction = true,
                actionLabel = context.getString(R.string.refresh),
            )

            // If the snackbar is dismissed, reset the boolean of the error-variable
            // The snackbar will reappear if we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {
                    mapListViewModel.refreshPlaces()
                }
                // If you click somewhere on the screen
                SnackbarResult.Dismissed -> {
                    // Check if in map or list
                    mapListViewModel.snackbarDismissed()
                }
            }
        }
    }

    /*
    TODO: These belong to searchbar
     */
    //var text by remember { mutableStateOf("") }
    //var active by remember { mutableStateOf(false) }
    Scaffold (
        topBar = {
            SkumringTopBar(
                title = stringResource(id = MapListDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        bottomBar = {
            SkumringBottomBar(navController = navController)
        }
    ) {innerPadding ->
        Column (modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            MapListContent(navController = navController, mapListViewModel = mapListViewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
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

    ToggleButtonThemeSwitcher (
        mapTheme = mapListUiState.mapListToggle.stateAsBool,
        onClick = { mapListViewModel.toggleMapListState() }
    )
    Surface {
        MapArea(
            navController = navController,
            mapListUiState = mapListUiState,
            mapListViewModel = mapListViewModel
        )
        if (mapListUiState.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mapListViewModel.hideBottomSheet() },
                sheetState = mapListUiState.sheetState,
                dragHandle = {}
                ) {
                BottomSheetContent(
                    place = mapListUiState.places.find { it.id == mapListUiState.clickedId }!!,
                    navController = navController,
                    mapListViewModel = mapListViewModel
                    )
            }
        }

        if (mapListUiState.mapListToggle == MapListToggleState.LIST) {
            Surface (color = MaterialTheme.colorScheme.background ){
                // Column for list view
                Column (Modifier.verticalScroll(rememberScrollState())) {
                    mapListUiState.places.forEach {place ->
                        ListCard(
                            name = place.name,
                            description = place.description,
                            isFavourite = place.isFavourite,
                            isCustom = place.isCustomPlace,
                            onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                                navController.navigate("placeinfoscreen/${place.id}")
                            },
                            onFavouriteClick = {mapListViewModel.toggleFavourite(place)},
                            imageToDisplay = "holmenkollen.jpg"
                        )
                    }
                }
            }
        }
    }
}

/**
 * Preview function for ToggleButtonThemeSwitcher
 */
@Preview
@Composable
fun ToggleButtonThemeSwitcherPreview() {
    var isMapTheme by remember { mutableStateOf(false) }
    ToggleButtonThemeSwitcher(
        mapTheme = isMapTheme,
        onClick = { isMapTheme = !isMapTheme }
    )
}

/**
 * Togglebutton that switches between Map view and List view
 */
@Composable
fun ToggleButtonThemeSwitcher(
    mapTheme: Boolean = false,
    borderWidth: Dp = 1.dp,
    parentShape: RoundedCornerShape = RoundedCornerShape(20.dp), // shape of the buttons
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val buttonHeightFactor = 0.15f //Height of the button
    val buttonHeight = screenWidth * buttonHeightFactor //how high the button is based on the screenwidth
    val buttonWidth =  screenWidth //Width of the button

    //how much the togglebutton should offset to the side based on size of the screen
    val offsetSmallScreen by animateDpAsState(
        targetValue = if (mapTheme) 0.dp else (buttonWidth/2 - buttonWidth/24),
        animationSpec = animationSpec, label = ""
    )

    //how much the togglebutton should offset to the side based on size of the screen
    val offsetLargeScreen by animateDpAsState(
        targetValue = if (mapTheme) 0.dp else (buttonWidth/2 - buttonWidth/50),
        animationSpec = animationSpec, label = ""
    )

BoxWithConstraints {
    if (maxWidth < 400.dp) {
        // button container
        Box(
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight)
                .clip(shape = parentShape)
                .clickable { onClick() }
                .background(MaterialTheme.colorScheme.onSecondary)
        ) {
            // toggle animation
            Box(
                modifier = Modifier
                    .width(buttonWidth / 2) // size of the toggle button
                    .height(buttonHeight)
                    .offset(x = offsetSmallScreen)
                    .clip(shape = parentShape)
                    .background(MaterialTheme.colorScheme.secondary)

            )
            // the icons and text representing list and map views
            Row(
                modifier = Modifier
                    .border(
                        border = BorderStroke(
                            width = borderWidth,
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        shape = parentShape
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    //List and list icon
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .width(buttonWidth / 2)
                        .height(buttonHeight)
                        .padding(start = 80.dp),
                ) {
                    Icon(
                        modifier = Modifier
                            .offset((-30).dp),
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Theme Icon",
                        tint = if (mapTheme) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = stringResource(R.string.toggle_list),
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (mapTheme) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.secondary
                    )
                }
                Box( //map and map icon
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(buttonWidth / 2)
                        .height(buttonHeight)
                ) {
                    Icon(
                        modifier = Modifier
                            .offset((-30).dp, 0.dp),
                        imageVector = Icons.Default.Place,
                        contentDescription = "place icon",
                        tint = if (mapTheme) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = stringResource(R.string.toggle_map),
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (mapTheme) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    } else { //if screen is bigger
        Box(
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight / 2)
                .clip(shape = parentShape)
                .clickable { onClick() }
                .background(MaterialTheme.colorScheme.onSecondary)
        ) {
            // toggle animation
            Box(
                modifier = Modifier
                    .width(buttonWidth / 2) // size of the toggle button
                    .height(buttonHeight / 2)
                    .offset(x = offsetLargeScreen)
                    .clip(shape = parentShape)
                    .background(MaterialTheme.colorScheme.secondary)
            )
            // the icons and text representing list and map views
            Row(
                modifier = Modifier
                    .border(
                        border = BorderStroke(
                            width = borderWidth,
                            color = MaterialTheme.colorScheme.secondary
                        ),
                        shape = parentShape
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    //List and list icon
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(buttonWidth / 2)
                        .height(buttonHeight)
                ) {
                    Icon(
                        modifier = Modifier
                            .offset((-30).dp)
                            .padding(start = 10.dp),
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Theme Icon",
                        tint = if (mapTheme) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = "List", // TODO xml
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (mapTheme) MaterialTheme.colorScheme.onSecondary
                        else MaterialTheme.colorScheme.secondary
                    )
                }
                Box( //map and map icon
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(buttonWidth / 2)
                        .height(buttonHeight)
                ) {
                    Icon(
                        modifier = Modifier
                            .offset((-30).dp, 0.dp),
                        imageVector = Icons.Default.Place,
                        contentDescription = "place icon",
                        tint = if (mapTheme) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSecondary
                    )
                    Text(
                        modifier = Modifier.padding(start = 15.dp),
                        text = "Map", // TODO xml
                        style = MaterialTheme.typography.headlineSmall,
                        color = if (mapTheme) MaterialTheme.colorScheme.secondary
                        else MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
    }
}

@Composable
fun BottomSheetContent(
    place: PlaceInfo,
    navController: NavController,
    mapListViewModel: MapListViewModel
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    )
    {
        ListCard(
            name = place.name,
            description = place.description,
            isFavourite = place.isFavourite,
            isCustom = false,
            onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                mapListViewModel.hideBottomSheet()
                navController.navigate("placeinfoscreen/${place.id}")
            },
            onFavouriteClick = {
                mapListViewModel.toggleFavourite(place = place)
            },
            imageToDisplay = "holmenkollen.jpg"
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

/**
 * Placeholder for the map display area
 */
@OptIn(MapboxExperimental::class)
@Composable
fun MapArea(mapListUiState: MapListUiState,
            navController: NavController,
            mapListViewModel: MapListViewModel) {

    val userPoint = Point.fromLngLat(mapListUiState.userLong.toDouble(), mapListUiState.userLat.toDouble())
    val context = LocalContext.current

    MapboxMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(16.dp)),
        mapInitOptionsFactory = { context ->
            MapInitOptions(
                context = context,
                styleUri = Style.OUTDOORS,
            )
        },
        onMapLongClickListener = {
            Log.d(logTag, "Long pressed. Long: ${it.longitude()}, Lat: ${it.latitude()}")
            true
        },
        ) {

        if (mapListUiState.userLocUpdated) {
            MapEffect { mapView ->
                Log.d(logTag, "userloc updated")
                mapView.camera.flyTo(
                    cameraOptions = CameraOptions.Builder()
                        .center(userPoint)
                        .zoom(10.0)
                        .build()
                )
            }
        }

        PointAnnotationGroup(
            annotations = mapListUiState.pins.map {pinInfo ->
                val long = pinInfo.long.toDouble()
                val lat = pinInfo.lat.toDouble()
                val point = Point.fromLngLat(long, lat)

                val iconImageBitmap = AppCompatResources.getDrawable(context, R.drawable.place_location_pin)!!.toBitmap()

                PointAnnotationOptions()
                    .withPoint(point)
                    .withIconImage(iconImageBitmap)
                    .withData(JsonPrimitive(pinInfo.id.toString()))
                    .withIconAnchor(IconAnchor.BOTTOM)
            },
            annotationConfig = AnnotationConfig(
                annotationSourceOptions = AnnotationSourceOptions(
                    clusterOptions = ClusterOptions(
                        colorLevels = listOf(Pair(0, Color.RED)),
                        textSize = 16.0,
                    )
                )
            ),
            onClick = {
                val lat = it.point.latitude().toString()
                val long = it.point.longitude().toString()
                val id = it.getData()!!.asString
                Log.d(logTag, "Clicked on pin with id: $id")
                mapListViewModel.showBottomSheet(id.toInt())
                //navController.navigate("infoscreen/${lat}/${long}/${id}")
                true
            }
        )
        // User location
        PointAnnotation(
            point = userPoint,
            iconImageBitmap = AppCompatResources.getDrawable(context, R.drawable.user_location_puck)!!.toBitmap()
        )

    }
}