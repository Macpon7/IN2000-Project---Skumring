package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mapandlist

import android.annotation.SuppressLint
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
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
        @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
fun MapAndListScreen() {
    var mapTheme by remember { mutableStateOf(false) }

    /*
    These belong to searchbar
     */
    //var text by remember { mutableStateOf("") }
    //var active by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Enable scrolling
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        ListAndMapButton(
            mapTheme = mapTheme,
            onThemeUpdated = { mapTheme = !mapTheme }
        )

        if (mapTheme) {
            // Column for map view
            Column(Modifier.fillMaxSize()) {
                MapArea()
            }
        } else {
            // Column for list view
            Column(Modifier.fillMaxSize()) {
                // Content for list view
            }
        }

    }
}


/**
 * Creates a button with two states, list view and map view
 */
@Composable
fun ListAndMapButton(mapTheme: Boolean, onThemeUpdated: () -> Unit) {
    Spacer(modifier = Modifier.height(40.dp))
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
@Composable
fun MapArea() {
    Box(
        modifier = Modifier
            .width(500.dp)
            .height(600.dp)
            .padding(6.dp)
            .background(Color.LightGray, RoundedCornerShape((16.dp))),
    ) {
        Text(
            text = "Map Display Placeholder",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


/**
 * Preview of the screen
 */
@Preview
@Composable
fun MapAndListPreview() {
    MapAndListScreen()
}