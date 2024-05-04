package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar

object SettingsScreenDestination : NavigationDestination {
    override val icon = null
    override val buttonTitle = null
    override val route = "Settings"
    override val titleRes = null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = viewModel(factory = SettingsViewModel.Factory),
    navController: NavHostController
) {
    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    //Variable for using strings in not-composable
    val context = LocalContext.current

    // Check if there is an error, if so show a snackbar:
    if (settingsUiState.showSnackbar) {
        LaunchedEffect(settingsUiState.snackbarHostState) {
            val result = settingsUiState.snackbarHostState.showSnackbar(
                message = settingsUiState.errorMessage,
                withDismissAction = true,
                actionLabel = context.getString(R.string.refresh),
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
            SkumringTopBar(title = stringResource(R.string.settings),
                canNavigateBack = true,
                navigateUp = { navController.popBackStack() })
        },
        bottomBar = {
            SkumringBottomBar(navController = navController)
        },
        snackbarHost = { SnackbarHost(hostState = settingsUiState.snackbarHostState) },
    ) { innerPadding -> //Here is what will be shown inside the scaffold of the screen
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()) //makes the column scrollable
                .padding(innerPadding)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            ContentSettings(settingsViewModel)
        }
    }
}

/**
 * Functions for the content of settingscreen:
 * All shown in dropdownmenu:
 * ChooseTheme
 * ChooseLanguage
 * ChooseStartLocation
 * ChooseLocationAs
 */
@Composable
fun ContentSettings(settingsViewModel: SettingsViewModel) {
    Column(modifier = Modifier.padding(30.dp)) {

        // Global variable for color of the text
        val TextColor: Color = MaterialTheme.colorScheme.onSurface

        // Colors for Textfield in dropdownmenu:
        val FocusedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer
        val UnfocusedContainerColor: Color = MaterialTheme.colorScheme.primaryContainer

        // Content for choose mode:
        ChooseTheme(
            settingsViewModel = settingsViewModel,
            textColor = TextColor,
            focusedContainerColor = FocusedContainerColor,
            unfocusedContainerColor = UnfocusedContainerColor
        )

        // Content for choosing language:
        ChooseLanguage(
            settingsViewModel = settingsViewModel,
            textColor = TextColor,
            focusedContainerColor = FocusedContainerColor,
            unfocusedContainerColor = UnfocusedContainerColor
        )

        // Content for choosing StartLocation:
        ChooseStartLocation(
            settingsViewModel = settingsViewModel,
            textColor = TextColor,
            focusedContainerColor = FocusedContainerColor,
            unfocusedContainerColor = UnfocusedContainerColor
        )
    }
}

/**
 * Function to decide if the user want dark or lightmode
 * Alternatives: Dark, light, follow system(default)
 * Shown as a dropdownmenu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTheme(
    settingsViewModel: SettingsViewModel,
    textColor: Color,
    focusedContainerColor: Color,
    unfocusedContainerColor: Color
) {

    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    ExposedDropdownMenuBox(
        expanded = settingsUiState.dropdownExpandedTheme,
        onExpandedChange = { settingsViewModel.expandDropdownTheme() }) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(
                    bottom = 16.dp,
                    top = 16.dp
                ),
            readOnly = true,
            value = settingsUiState.selectedDropDownOptionTheme, // TODO change text color?
            onValueChange = {},
            label = {
                Text(
                    text = stringResource(R.string.choose_theme),
                    color = textColor,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = settingsUiState.dropdownExpandedTheme
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = focusedContainerColor,
                unfocusedContainerColor = unfocusedContainerColor
            ),
            /*
            leadingIcon = {
                Icon( // TODO
                    imageVector = settingsUiState.theme.iconImageVector,
                    contentDescription = "FOLLOW_SYSTEM"
                )
            }
             */
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = settingsUiState.dropdownExpandedTheme,
            onDismissRequest = { settingsViewModel.expandDropdownTheme() }
        ) {
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.follow_system),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                },
                onClick = {
                    settingsViewModel.updateTheme(
                        theme = Theme.FOLLOW_SYSTEM
                    )
                },
                /*
                leadingIcon = {
                    Icon( // TODO
                        imageVector = Icons.Outlined.Settings,
                        contentDescription = "FOLLOW_SYSTEM"
                    )
                }
                 */
            )
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.light_mode),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                },
                onClick = {
                    settingsViewModel.updateTheme(
                        theme = Theme.LIGHT_MODE
                    )
                }
            )
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.dark_mode),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                },
                onClick = {
                    settingsViewModel.updateTheme(
                        theme = Theme.DARK_MODE
                    )
                }
            )
        }
    }
}

@Composable
@Preview
fun PreviewChooseTheme(settingsViewModel: SettingsViewModel = viewModel()) {
    //ChooseTheme(settingsViewModel)
}

/**
 * Function to decide for norwegian or english language
 * Can also choose to follow the system
 * Alternatives: Follow system, english, norwegian
 * Shown as a dropdownmenu
 */
@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseLanguage(
    settingsViewModel: SettingsViewModel,
    textColor: Color,
    focusedContainerColor: Color,
    unfocusedContainerColor: Color
) {

    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    ExposedDropdownMenuBox(
        expanded = settingsUiState.dropdownExpandedLanguage,
        onExpandedChange = { settingsViewModel.expandDropdownLanguage() }) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            readOnly = true,
            value = settingsUiState.selectedDropDownOptionLanguage,
            onValueChange = {},
            label = {
                Text(
                    text = stringResource(R.string.choose_language),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = settingsUiState.dropdownExpandedLanguage
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = focusedContainerColor,
                unfocusedContainerColor = unfocusedContainerColor
            ),
        )
        ExposedDropdownMenu(
            expanded = settingsUiState.dropdownExpandedLanguage,
            onDismissRequest = { settingsViewModel.expandDropdownLanguage() }
        ) {
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.follow_system),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                },
                onClick = {
                    settingsViewModel.updateLanguage(
                        language = Language.FOLLOW_SYSTEM
                    )
                }
            )
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.english),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                },
                onClick = {
                    settingsViewModel.updateLanguage(
                        language = Language.ENGLISH,
                    )
                }
            )
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.norwegian),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                },
                onClick = {
                    settingsViewModel.updateLanguage(
                        language = Language.NORWEGIAN,
                    )
                }
            )
        }
    }
}

@Composable
@Preview
fun PreviewChooseLanguage(settingsViewModel: SettingsViewModel = viewModel()) {
    //ChooseLanguage(settingsViewModel)
}

/**
 * Function to show a chosen location or phones position as default
 * Alternatives: Costum location, phone's location
 * Shown as a dropdownmenu
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseStartLocation(
    settingsViewModel: SettingsViewModel,
    textColor: Color,
    focusedContainerColor: Color,
    unfocusedContainerColor: Color
) {

    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    ExposedDropdownMenuBox(
        expanded = settingsUiState.dropdownExpandedStartLocation,
        onExpandedChange = { settingsViewModel.expandDropdownStartLocation() }) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            readOnly = true,
            value = settingsUiState.selectedDropDownOptionLocation,
            onValueChange = {},
            label = {
                Text(
                    text = stringResource(R.string.choose_default_location),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = settingsUiState.dropdownExpandedStartLocation
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = focusedContainerColor,
                unfocusedContainerColor = unfocusedContainerColor
            )
        )
        ExposedDropdownMenu(
            expanded = settingsUiState.dropdownExpandedStartLocation,
            onDismissRequest = { settingsViewModel.expandDropdownStartLocation() }
        ) {
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.costum_location),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                },
                onClick = {
                    settingsViewModel.updateSelectedDefaultLocation(
                        location = Location.COSTUM_LOCATION
                    )

                    // TODO show a dropdown meny or a searchbar to choose location?
                }
            )
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.phones_location),
                        style = MaterialTheme.typography.bodyLarge,
                        color = textColor
                    )
                },
                onClick = {
                    settingsViewModel.updateSelectedDefaultLocation(
                        location = Location.PHONES_LOCATION
                    )
                }
            )
        }
    }
}

@Composable
@Preview
fun PreviewStartLocation(settingsViewModel: SettingsViewModel = viewModel()) {
    //ChooseStartLocation(settingsViewModel)
}

@Composable
@Preview
fun PreviewSettingsScreen(navController: NavHostController = rememberNavController()) {
    SettingsScreen(navController = navController)
}