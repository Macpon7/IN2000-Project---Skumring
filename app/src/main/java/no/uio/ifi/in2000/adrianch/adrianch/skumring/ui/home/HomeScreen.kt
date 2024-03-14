package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.SkumringTopAppBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme

object HomeDestination : NavigationDestination {//This one is used in the SkumringButtonBar to choose destination
    override val icon = Icons.Outlined.Home //Show home-icon
    override val buttonTitle = R.string.nav_home_button //This is in Int, have to use stringResource to get the String from string.xml
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
    val homeUiState: HomeUiState by homeViewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            SkumringTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        }

    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        ContentHomeScreen()
    }
}

/**
 * Function with alle the content of the homescreen
 * This exclude the top- and bottomBar
 */
@Composable
fun ContentHomeScreen() {
    Column {

        SunTempAndTime()

        SunDown()

        Text(text = SunCheck(Good = false))

        //This should be at the Bottom, with padding:
        MapBox()
    }

}

/**
 * The sun is a picture and in front it should be text of the time and the temperature
 */
@Composable
fun SunTempAndTime() {
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
 * Shows icon of the sun going down
 * Show text of when the sun goes down
 */
@Composable
fun SunDown() {
    Box(
        modifier = Modifier,
    ) {
        Text(
            text = "Map Display Placeholder",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

fun SunCheck(Good : Boolean) : String {
    return if (Good) {
        "Det er gode forhold for solnedgang idag!"
    }
    else {
        "Det er drittvær idag"
    }
}

/**
 * Function with the map inside
 * Should be put inside the contentHomeScreen
 */
@Composable
fun MapBox() {
    Box(
        modifier = Modifier
            .width(500.dp)
            .height(600.dp)
            .padding(6.dp)
            .background(Color.LightGray, RoundedCornerShape((16.dp))),
    ) {
        Text(
            text = "Sun display placeholder",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
@Preview
fun Test() {
    HomeScreen()
}