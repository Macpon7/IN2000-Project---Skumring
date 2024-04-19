package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceInfo
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
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

    // Check if there is an error, if so show a snackbar:
    if (mapListUiState.showSnackbar) {
        LaunchedEffect(mapListUiState.snackbarHostState) {
            val result = mapListUiState.snackbarHostState.showSnackbar(
                message = mapListUiState.errorMessage,
                withDismissAction = true,
                actionLabel = "Refresh",
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

    ThemeSwitcher (
        mapTheme = mapListUiState.mapListToggle.stateAsBool,
        size = 65.dp, //Size of the button
        padding = 3.dp,
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
                            onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                                navController.navigate("placeinfoscreen/${place.id}")
                            }
                        )
                    }
                }
            }
        }
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
            onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                mapListViewModel.hideBottomSheet()
                navController.navigate("placeinfoscreen/${place.id}")
            }
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}

/**
 * Placeholder for the map display area
 */
@OptIn(MapboxExperimental::class)
@Composable
fun MapArea(mapListUiState: MapListUiState, navController: NavController, mapListViewModel: MapListViewModel) {
    // Can declare point to contain current location of user
    val testPoint = Point.fromLngLat(10.71839307051461, 59.943735106220444)
    //var point: Point by remember { mutableStateOf(testPoint) }
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
                cameraOptions = CameraOptions.Builder()
                    .center(Point.fromLngLat(10.71839307051461, 59.943735106220444))
                    .zoom(10.0)
                    .build()
            )
        },
        onMapLongClickListener = {
            Log.d(logTag, "Long pressed. Long: ${it.longitude()}, Lat: ${it.latitude()}")
            true
        }
    ) {
        PointAnnotationGroup(
            annotations = mapListUiState.pins.map {pinInfo ->
                val long = pinInfo.long.toDouble()
                val lat = pinInfo.lat.toDouble()
                val point = Point.fromLngLat(long, lat)

                val iconImageBitmap = AppCompatResources.getDrawable(context, R.drawable.location_on)!!.toBitmap()

                PointAnnotationOptions()
                    .withPoint(point)
                    .withIconImage(iconImageBitmap)
                    .withData(JsonPrimitive(pinInfo.id.toString()))
            },
            annotationConfig = AnnotationConfig(
                annotationSourceOptions = AnnotationSourceOptions(
                    clusterOptions = ClusterOptions()
                )
            ),
            onClick = {
                val id = it.getData()!!.asString
                Log.d(logTag, "Clicked on pin with id: $id")
                mapListViewModel.showBottomSheet(id.toInt())
                //navController.navigate("infoscreen/${lat}/${long}/${id}")
                true
            }
        )
    }
}

/**
 * Cards with information about places
 */
@Composable
fun ListCard(name: String, description: String, onItemClick: () -> Unit) {
    BoxWithConstraints {
        if (maxWidth < 400.dp) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable(onClick = onItemClick), //Click to infoscreen
            ){

                //Box for picture:
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.LightGray, RoundedCornerShape((0.dp)))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Image Placeholder",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                )
                {
                    Text(
                        text = name,
                        modifier = Modifier
                            .padding(vertical = 2.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "")
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Notifications, contentDescription = "")
                        }
                    }
                }

                //Text for description. Do we want weather condition in the future?
                Text(
                    text = description,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .padding(bottom = 4.dp)
                        .align(Alignment.CenterHorizontally))
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clickable(onClick = onItemClick), //Click to infoscreen
            ){

                //Box for picture:
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .background(Color.LightGray, RoundedCornerShape((0.dp)))
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Image Placeholder",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                Row (
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp)
                )
                {
                    Text(
                        text = name,
                        modifier = Modifier
                            .padding(vertical = 2.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.FavoriteBorder, contentDescription = "")
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(imageVector = Icons.Filled.Notifications, contentDescription = "")
                        }
                    }
                }

                //Text for description. Do we want weather condition in the future?
                Text(
                    text = description,
                    modifier = Modifier
                        .padding(vertical = 2.dp)
                        .padding(bottom = 4.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}