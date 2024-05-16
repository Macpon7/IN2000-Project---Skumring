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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
import com.mapbox.maps.plugin.compass.generated.CompassSettings
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.database.AppDatabase
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.place.PlaceRepositoryImpl
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.AirConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.CloudConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.forecast.WeatherConditionsRating
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.place.SunEvent
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.dialogs.NewPlaceDialog
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.ListCard
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.WeatherIconCheck
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val logTag = "MapListScreen"

object MapListDestination : NavigationDestination {
    override val icon = Icons.Outlined.Place
    override val buttonTitle = R.string.nav_map_button
    override val route = "maplist"
    override val titleRes = R.string.find_loaction
}

/**
 * Main composable function for displaying the map screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapListScreen(
    navController: NavHostController,
    mapListViewModel: MapListViewModel = viewModel(factory = MapListViewModel.Factory)
) {
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
            Log.d(logTag, "Showing snackbar")
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

    Scaffold(
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
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
fun MapListContent(navController: NavController, mapListViewModel: MapListViewModel) {
    val mapListUiState: MapListUiState by mapListViewModel.mapListUiState.collectAsState()

    if (mapListUiState.showNewPlaceDialog) {
        NewPlaceDialog(
            hideDialog = { mapListViewModel.hideNewPlaceDialog() },
            onEvent = { mapListViewModel.onNewPlaceDialogEvent(it) },
            getCoordinatesFromAddress = mapListViewModel.getCoordinatesFromAddress,
            getCoordinatesFromLocation = mapListViewModel.getCoordinatesFromUserLocation,
            addCustomPlace = mapListViewModel.addPlace,
            uiStateFlow = mapListViewModel.newPlaceUiState
        )
    }

    ToggleButtonThemeSwitcher(
        mapTheme = mapListUiState.mapListToggle.stateAsBool,
        onClick = { mapListViewModel.toggleMapListState() }
    )
    Surface {
        MapArea(
            mapListUiState = mapListUiState,
            mapListViewModel = mapListViewModel
        )
        if (mapListUiState.showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { mapListViewModel.hideBottomSheet() },
                sheetState = mapListUiState.sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                BottomSheetContent(
                    place = mapListUiState.places.find { it.id == mapListUiState.clickedId }!!,
                    navController = navController,
                    toggleFavourite = mapListViewModel::toggleFavourite,
                    onDismissRequest = mapListViewModel::hideBottomSheet
                )
            }
        }
        if (mapListUiState.mapListToggle == MapListToggleState.LIST) {
            Surface(color = MaterialTheme.colorScheme.background) {

                if (mapListUiState.places.isEmpty()) {
                    Column (modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(R.string.no_location),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    // Column for list view
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        mapListUiState.places.forEach { place ->
                            ListCard(
                                place = place,
                                onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                                    navController.navigate("placeinfoscreen/${place.id}")
                                },
                                onFavouriteClick = { mapListViewModel.toggleFavourite(place) }
                            )
                        }
                    }
                }
            }
        }
    }
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
    val buttonHeight =
        screenWidth * buttonHeightFactor //how high the button is based on the screenwidth

    //how much the togglebutton should offset to the side based on size of the screen
    val offsetSmallScreen by animateDpAsState(
        targetValue = if (mapTheme) 0.dp else (screenWidth / 2 - screenWidth / 24),
        animationSpec = animationSpec, label = ""
    )

    //how much the togglebutton should offset to the side based on size of the screen
    val offsetLargeScreen by animateDpAsState(
        targetValue = if (mapTheme) 0.dp else (screenWidth / 2 - screenWidth / 50),
        animationSpec = animationSpec, label = ""
    )

    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            // button container
            Box(
                modifier = Modifier
                    .width(screenWidth)
                    .height(buttonHeight)
                    .clip(shape = parentShape)
                    .clickable { onClick() }
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                // toggle animation
                Box(
                    modifier = Modifier
                        .width(screenWidth / 2) // size of the toggle button
                        .height(buttonHeight)
                        .offset(x = offsetSmallScreen)
                        .clip(shape = parentShape)
                        .background(MaterialTheme.colorScheme.onTertiary)

                )
                // the icons and text representing list and map views
                Row(
                    modifier = Modifier
                        .border(
                            border = BorderStroke(
                                width = borderWidth,
                                color = MaterialTheme.colorScheme.onTertiary
                            ),
                            shape = parentShape
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        //List and list icon
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .width(screenWidth / 2)
                            .height(buttonHeight)
                            .padding(start = 80.dp),
                    ) {
                        Icon(
                            modifier = Modifier
                                .offset((-30).dp),
                            imageVector = Icons.Default.Menu,
                            contentDescription = stringResource(R.string.toggle_list_icon),
                            tint = if (mapTheme) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onTertiary
                        )
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = stringResource(R.string.toggle_list),
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (mapTheme) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onTertiary
                        )
                    }
                    Box( //map and map icon
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(screenWidth / 2)
                            .height(buttonHeight)
                    ) {
                        Icon(
                            modifier = Modifier
                                .offset((-30).dp, 0.dp),
                            imageVector = Icons.Default.Place,
                            contentDescription = stringResource(R.string.toggle_map_icon),
                            tint = if (mapTheme) MaterialTheme.colorScheme.onTertiary
                            else MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            modifier = Modifier.padding(start = 15.dp),
                            text = stringResource(R.string.toggle_map),
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (mapTheme) MaterialTheme.colorScheme.onTertiary
                            else MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        } else { //if screen is bigger
            Box(
                modifier = Modifier
                    .width(screenWidth)
                    .height(buttonHeight / 2)
                    .clip(shape = parentShape)
                    .clickable { onClick() }
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                // toggle animation
                Box(
                    modifier = Modifier
                        .width(screenWidth / 2) // size of the toggle button
                        .height(buttonHeight / 2)
                        .offset(x = offsetLargeScreen)
                        .clip(shape = parentShape)
                        .background(MaterialTheme.colorScheme.onTertiary)
                )
                // the icons and text representing list and map views
                Row(
                    modifier = Modifier
                        .border(
                            border = BorderStroke(
                                width = borderWidth,
                                color = MaterialTheme.colorScheme.onTertiary
                            ),
                            shape = parentShape
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        //List and list icon
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(screenWidth / 2)
                            .height(buttonHeight)
                    ) {
                        Icon(
                            modifier = Modifier
                                .offset((-30).dp, 0.dp),
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Theme Icon",
                            tint = if (mapTheme) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onTertiary
                        )
                        Text(
                            modifier = Modifier.padding(start = 15.dp),
                            text = stringResource(R.string.toggle_list),
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (mapTheme) MaterialTheme.colorScheme.tertiary
                            else MaterialTheme.colorScheme.onTertiary
                        )
                    }
                    Box( //map and map icon
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .width(screenWidth / 2)
                            .height(buttonHeight)
                    ) {
                        Icon(
                            modifier = Modifier
                                .offset((-30).dp, 0.dp),
                            imageVector = Icons.Default.Place,
                            contentDescription = stringResource(R.string.toggle_map_icon),
                            tint = if (mapTheme) MaterialTheme.colorScheme.onTertiary
                            else MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            modifier = Modifier.padding(start = 15.dp),
                            text = stringResource(R.string.toggle_map),
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (mapTheme) MaterialTheme.colorScheme.onTertiary
                            else MaterialTheme.colorScheme.tertiary
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
    toggleFavourite: (PlaceInfo) -> Unit,
    onDismissRequest: () -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 0.dp, bottom = 20.dp, start = 12.dp, end = 12.dp)
    ) {
        //name of place and favourite button
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        )
        {
            Text(
                text = place.name,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            IconButton(onClick = {
                toggleFavourite(place)
            }
            ) {
                if (place.isFavourite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = stringResource(id = R.string.favourite_icon),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(60.dp)

                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(R.string.favourite_icon),
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(60.dp)
                    )

                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        )
        {
            //sunset today string
            Text(
                text = stringResource(id = R.string.home_sunset),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Divider(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                thickness = 1.dp,
                modifier = Modifier.padding(start = 80.dp, end = 80.dp)
            )
            //time for sunset
            Text(
                text = stringResource(R.string.time) + ": ${
                    place.sunEvents[0].time.format(
                        DateTimeFormatter.ofPattern("HH':'mm")
                    )
                }",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 10.dp)
            )
            //temerature at sunset
            Text(
                text = stringResource(R.string.temp_at_sunset) + ": ${place.sunEvents[0].tempAtEvent} °C",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
            //Conditions at sunset
            Text(
                text = stringResource(R.string.weather_condition) + stringResource(id = place.sunEvents[0].conditions.weatherRating.stringResourceId),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(bottom = 10.dp)
            )
            //Weather icon
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .padding(bottom = 10.dp)
            ) {
                WeatherIconCheck(
                    weatherCondition = place.sunEvents[0].weatherIcon,
                    weather = place.sunEvents[0].conditions.weatherRating
                )
            }
            //description string
            Text(
                text = stringResource(R.string.new_place_description),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Divider(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                thickness = 1.dp,
                modifier = Modifier.padding(start = 80.dp, end = 80.dp, bottom = 10.dp)
            )
            //description about the place
            Text(
                text = place.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))

            //close button and more information button
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            )
            {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    },
                    contentPadding = PaddingValues(12.dp),
                ) {
                    Text(
                        text = stringResource(R.string.close),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
                Button(
                    onClick = {
                        onDismissRequest()
                        navController.navigate("placeinfoscreen/${place.id}")
                    },
                    contentPadding = PaddingValues(12.dp),
                    modifier = Modifier.padding(start = 15.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),

                    ) {
                    Text(
                        text = stringResource(R.string.home_more_details_button),
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge,

                        )
                }

            }
        }
    }
}


/**
 * Map display area
 */
@OptIn(MapboxExperimental::class)
@Composable
fun MapArea(
    mapListUiState: MapListUiState,
    mapListViewModel: MapListViewModel
) {
    // Loading in user location, checking that it has been updated from default.
    // If not, handling so we get a nice display over Norway.
    val userLong = mapListUiState.userLong.toDouble()
    val userLat = mapListUiState.userLat.toDouble()
    val userPoint: Point
    val userZoom: Double
    if (userLong == 0.0 && userLat == 0.0) {
        userPoint = Point.fromLngLat(10.0, 60.0)
        userZoom = 3.0
    } else {
        userPoint = Point.fromLngLat(userLong, userLat)
        userZoom = 10.0
    }
    val context = LocalContext.current
    val style: String = if (isSystemInDarkTheme()) {
        Style.DARK
    } else {
        Style.OUTDOORS
    }

    MapboxMap(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp)
            .clip(RoundedCornerShape(16.dp)),
        mapInitOptionsFactory = { context ->
            MapInitOptions(
                context = context,
                styleUri = style,
            )
        },
        onMapLongClickListener = {
            Log.d(logTag, "Long pressed. Long: ${it.longitude()}, Lat: ${it.latitude()}")
            mapListViewModel.showNewPlaceDialog(lat = it.latitude().toString(), long = it.longitude().toString())
            true
        },
        compassSettings = CompassSettings {
            enabled = true
            visibility = true
            fadeWhenFacingNorth = false
        }
    ) {
        if (mapListUiState.userLocUpdated) {
            MapEffect { mapView ->
                Log.d(logTag, "userloc updated")
                mapView.camera.flyTo(
                    cameraOptions = CameraOptions.Builder()
                        .center(userPoint)
                        .zoom(userZoom)
                        .build()
                )
            }
        }

        PointAnnotationGroup(
            annotations = mapListUiState.pins.map { pinInfo ->
                val long = pinInfo.long.toDouble()
                val lat = pinInfo.lat.toDouble()
                val point = Point.fromLngLat(long, lat)

                val iconImageBitmap =
                    AppCompatResources.getDrawable(context, R.drawable.place_location_pin)!!
                        .toBitmap()

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
        // If userloc hasn't been updated from default, we don't bother creating user location icon
        if (userLong != 0.0 && userLat != 0.0) {
            PointAnnotation(
                point = userPoint,
                iconImageBitmap = AppCompatResources.getDrawable(
                    context,
                    R.drawable.user_location_puck
                )!!.toBitmap()
            )
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
 * Preview for the BottomSheet content
 */
@Preview
@Composable
fun BottomSheetPreview(navController: NavHostController = rememberNavController()) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context = context)
    val imageDao = database.imageDao()
    val forecastDao = database.forecastDao()
    val placeInfoDao = database.placeInfoDao()
    val placeRepository = PlaceRepositoryImpl(
        placeInfoDao = placeInfoDao,
        forecastDao = forecastDao,
        imageDao = imageDao,
        context = context
    )
    SkumringTheme {
        Surface {
            BottomSheetContent(
                place = PlaceInfo(
                    id = 123,
                    name = "Monrads gate 33",
                    description = "godt sted å ta bilde på en solrik dag",
                    lat = "",
                    long = "",
                    isFavourite = false,
                    isCustomPlace = false,
                    hasNotification = false,
                    images = emptyList(),
                    sunEvents = listOf(
                        SunEvent(
                            time = LocalDateTime.now(),
                            tempAtEvent = "20",
                            weatherIcon = ":)",
                            conditions = WeatherConditions(
                                weatherRating = WeatherConditionsRating.EXCELLENT,
                                cloudConditionLow = CloudConditions.CLEAR,
                                cloudConditionHigh = CloudConditions.CLEAR,
                                cloudConditionMedium = CloudConditions.CLEAR,
                                airCondition = AirConditions.LOW
                            ),
                            blueHourTime = LocalDateTime.now(),
                            goldenHourTime = LocalDateTime.now()
                        )
                    )
                ),
                navController = navController,
                onDismissRequest = {},
                toggleFavourite = {}
            )
        }
    }
}