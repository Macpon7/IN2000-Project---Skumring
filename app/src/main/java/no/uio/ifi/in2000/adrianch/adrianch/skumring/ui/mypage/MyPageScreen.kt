package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.SkumringTopAppBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination

object MyPageDestination : NavigationDestination {
    override val icon = Icons.Outlined.AccountCircle
    override val buttonTitle = R.string.nav_personal_button
    override val route = "mypage"
    override val titleRes = R.string.nav_personal_button
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen() {

    Scaffold(
        topBar = {
            SkumringTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false,
            )
        }

    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column (modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ContentMyPage()
        }
    }
}

@Composable
fun ContentMyPage(){
    Settings{
        // The action that happens when the button is pressed happens here:

    }
    
    var locationAdded = false

    AddLocationButton {
        // The action that happens when the button is pressed happens here:
        locationAdded = true
    }

    if (locationAdded) {
        Text("Ny plassering lagt til!")
    }
}

/**
 * Button to go into user-settings
 */
@Composable
fun Settings(onClick: () -> Unit){
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Push the icon to the right:
        Spacer(modifier = Modifier.weight(1f))
        IconButton(
            onClick = { onClick() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }
}

/**
 * Button for adding your own locations to my page
 * Will show a new "page" that allow you to add the location
 */
@Composable
fun AddLocationButton(onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
    ) {
        // Push the icon to the right:
        Spacer(modifier = Modifier.weight(1f))
        FloatingActionButton(
            onClick = { onClick() },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(Icons.Filled.Add, "Add location")
        }
    }
}

@Composable
@Preview
fun Test() {
    MyPageScreen()
}