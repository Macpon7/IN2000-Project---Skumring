package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination

object SettingsScreenDestination : NavigationDestination {
    override val icon = null
    override val buttonTitle = null
    override val route = "Settings"
    override val titleRes = null
}

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    navController: NavController
) {
    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    // Check if there is an error, if so show a snackbar:
    if (settingsUiState.showSnackbar) {
        LaunchedEffect(settingsUiState.snackbarHostState) {
            val result = settingsUiState.snackbarHostState.showSnackbar(
                message = settingsUiState.errorMessage,
                withDismissAction = true,
                actionLabel = "Refresh",
            )
            // If the snackbar is dismissed, reset the boolean of the showSnackbar-variable
            // The snackbar will reappear is we get a new error
            when (result) {
                // If you press refresh
                SnackbarResult.ActionPerformed -> {
                    settingsViewModel.refresh()
                }
                // If you click somewhere on the screen
                SnackbarResult.Dismissed -> {
                    settingsViewModel.snackbarDismissed()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .padding(vertical = 10.dp) // Choose the size of the topbar
            ) {
                IconButton(onClick = {navController.popBackStack()},
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Arrow back" ,
                        modifier = Modifier.size(30.dp) // Set the size of the Icon inside the IconButton
                    )
                }
                Text(
                    text = "Settings", //TODO -> make in xml
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        snackbarHost = { SnackbarHost(hostState = settingsUiState.snackbarHostState) },
    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column (modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ContentSettings()
        }
    }
}

@Composable
fun ContentSettings() {
    Text(text = "Settings")


}

@Composable
@Preview
fun Test() {
    ContentSettings()
}