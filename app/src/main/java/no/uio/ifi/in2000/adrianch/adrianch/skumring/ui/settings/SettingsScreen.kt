package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar

object SettingsScreenDestination : NavigationDestination {
    override val icon = null
    override val buttonTitle = null
    override val route = "Settings"
    override val titleRes = null
}

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(),
    navController: NavHostController
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
                    text = stringResource(R.string.settings),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        bottomBar = {
            SkumringBottomBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = settingsUiState.snackbarHostState) },
    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column (
            modifier = Modifier
                .verticalScroll(rememberScrollState()) //makes the column scrollable
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            ContentSettings(settingsViewModel)
        }
    }
}

@Composable
fun ContentSettings(settingsViewModel: SettingsViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

        // Content for notification:
        Notification(settingsViewModel = settingsViewModel)

        // Content for choose mode:
        ChooseMode(settingsViewModel = settingsViewModel)

        // Content for choosing language:
        ChooseLanguage(settingsViewModel = settingsViewModel)

        // Content for StartLocation:
        TextButton(onClick = {settingsViewModel.showStartLocationDialog() }) {
            Text(text = stringResource(R.string.choose_default_location))
        }
        if (settingsUiState.showDialog) {
            StartLocation(settingsViewModel = settingsViewModel)
        }
        Text(text = "${stringResource(R.string.default_location)}: ${settingsUiState.selectedDefaultLocation}",
            fontWeight = FontWeight.Bold)
    }
}

/**
 * Function for global notification on or off
 * Toggle button, where it says notification
 */
@Composable
fun Notification(settingsViewModel: SettingsViewModel) {
    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.notifications),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Bold
        )
        Switch(
            checked = settingsUiState.notificationEnabled,
            onCheckedChange = { isChecked -> settingsViewModel.updateNotificationEnabled(isChecked) },
            colors = SwitchDefaults.colors(
                // TODO change to better colors?
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                uncheckedTrackColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
@Preview
fun PreviewNotification(settingsViewModel: SettingsViewModel = viewModel()) {
    Notification(settingsViewModel)
}

/**
 * Function to decide if the user want dark or lightmode
 * Should be shown in a dropdownmeny
 * Alternatives: Dark, light, follow system(default)
 */
@Composable
fun ChooseMode(settingsViewModel: SettingsViewModel) {
    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    val modeOptions = listOf(stringResource(R.string.follow_system),
        stringResource(R.string.dark_mode),
        stringResource(R.string.light_mode)
    ) //TODO make enum class?

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = stringResource(R.string.mode),
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.Bold
        )
        modeOptions.forEach { mode ->
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = mode == settingsUiState.selectedMode,
                    onClick = {
                        settingsViewModel.updateSelectedMode(mode)
                    },
                )
                Text(
                    text = mode,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Text(
            text = "${stringResource(R.string.selected_mode)}: ${settingsUiState.selectedMode}",
        )
    }
}

@Composable
@Preview
fun PreviewChooseMode(settingsViewModel: SettingsViewModel = viewModel()) {
    ChooseMode(settingsViewModel)
}

/**
 * Function to decide for norwegian or english language
 */
@Composable
fun ChooseLanguage(settingsViewModel: SettingsViewModel) {
    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    val languageOptions = listOf(stringResource(R.string.norwegian), stringResource(R.string.english)) //TODO make enum class?

    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = stringResource(R.string.choose_language),
            modifier = Modifier.padding(bottom = 8.dp),
            fontWeight = FontWeight.Bold
        )
        languageOptions.forEach { language ->
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = language == settingsUiState.language,
                    onClick = { settingsViewModel.updateLanguage(language) },
                )
                Text(
                    text = language,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Text(
            text = "${stringResource(R.string.selected_language)}: ${settingsUiState.language}",
        )
    }
}

@Composable
@Preview
fun PreviewChooseLanguage(settingsViewModel: SettingsViewModel = viewModel()) {
    ChooseLanguage(settingsViewModel)
}

/**
 * Function to show a choosen location or phones position as default
 */
@Composable
fun StartLocation(settingsViewModel: SettingsViewModel) {
    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    // TODO xml
    val locationOptions = listOf("Chosen Location", "Phone's Position")

    Dialog(onDismissRequest = {settingsViewModel.hideStartLocationDialog()}) {
        Card {
            Column(modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Start Location", // TODO xml
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                locationOptions.forEach { location ->
                    Row(
                        modifier = Modifier.padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = location == settingsUiState.selectedDefaultLocation,
                            onClick = {
                                settingsViewModel.updateSelectedDefaultLocation(location)

                                if (location == "chosen location") {
                                    // TODO show a dropdown meny or a searchbar to choose location
                                }
                            },
                        )
                        Text(
                            text = location,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewStartLocation(    settingsViewModel: SettingsViewModel = viewModel(), ) {
    StartLocation(settingsViewModel)
}

@Composable
@Preview
fun PreviewSettingsScreen(navController: NavHostController = rememberNavController()) {
    SettingsScreen(navController = navController)
}