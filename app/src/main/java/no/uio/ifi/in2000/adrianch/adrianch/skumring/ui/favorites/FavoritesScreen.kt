package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites




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
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotationGroup
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.AnnotationSourceOptions
import com.mapbox.maps.plugin.annotation.ClusterOptions
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.data.placeinfo.presetPlacesDetails
import no.uio.ifi.in2000.adrianch.adrianch.skumring.model.placeinfo.PlaceSummary
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.ListCard
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar

private const val logTag = "FavoritesScreen"

object FavoritesDestination : NavigationDestination {
    override val icon = Icons.Outlined.FavoriteBorder
    override val buttonTitle = R.string.nav_fav_button
    override val route = "favorite"
    override val titleRes = R.string.app_name
}

/**
 * Main composable function for displaying the map screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController : NavHostController, favoritesViewModel: FavoritesViewModel = viewModel()) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val favoritesUiState: FavoritesUiState by favoritesViewModel.favoritesUiState.collectAsState()

    // Check if there is an error, if so show a snackbar:
    if (favoritesUiState.showSnackbar) {
        LaunchedEffect(favoritesUiState.snackbarHostState) {
            val result = favoritesUiState.snackbarHostState.showSnackbar(
                message = favoritesUiState.errorMessage,
                withDismissAction = true,
                actionLabel = "Refresh",
            )

            // If the snackbar is dismissed, reset the boolean of the error-variable
            // The snackbar will reappear is we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {

                    favoritesViewModel.refreshList()
                }
                // If you click somewhere on the screen
                SnackbarResult.Dismissed -> {
                    // Check if in map or list
                    favoritesViewModel.snackbarDismissed()
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
                title = stringResource(id = FavoritesDestination.titleRes),
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
            FavoriteListContent(navController = navController, favoriteViewModel = favoritesViewModel, favoritesUiState = favoritesUiState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteListContent(navController : NavController,
                        favoriteViewModel: FavoritesViewModel,
                        favoritesUiState: FavoritesUiState
                        ) {
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

    Column (Modifier.verticalScroll(rememberScrollState())) {
        if (favoritesUiState.places.isEmpty()){
            Text(text = "No Places")
        } else {
            favoritesUiState.places.forEach { place ->
                ListCard(
                    name = place.name,
                    description = place.description,
                    onItemClick = { //Navigate when it is clicked on. This needs to send lat, long, id
                        navController.navigate("placeinfoscreen/${place.lat}/${place.long}/${place.id}")
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewFavorites(navController: NavHostController = rememberNavController()) {
    FavoritesScreen(navController = navController)
}