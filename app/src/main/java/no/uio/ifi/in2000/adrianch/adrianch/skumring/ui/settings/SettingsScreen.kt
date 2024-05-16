package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringTopBar

object SettingsScreenDestination : NavigationDestination {
    override val icon = null
    override val buttonTitle = null
    override val route = "settings"
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

    LaunchedEffect(Unit) {
        settingsViewModel.readJSONSettings()
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
            ChooseTheme(
                settingsViewModel = settingsViewModel
            )
        }
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
) {

    val settingsUiState: SettingsUiState by settingsViewModel.settingsUiState.collectAsState()

    ExposedDropdownMenuBox(
        expanded = settingsUiState.isThemeDropdownExpanded,
        onExpandedChange = { settingsViewModel.toggleThemeDropdown() }) {
        TextField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .padding(
                    bottom = 16.dp, top = 16.dp
                ),
            readOnly = true,
            value = stringResource(id = settingsUiState.settings.theme.stringResourceId), // TODO change text color?
            onValueChange = {},
            label = {
                Text(
                    text = stringResource(R.string.choose_theme),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = settingsUiState.isThemeDropdownExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = settingsUiState.isThemeDropdownExpanded,
            onDismissRequest = { settingsViewModel.toggleThemeDropdown() }
        ) {
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.follow_system),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                onClick = {
                    settingsViewModel.updateTheme(
                        theme = Theme.FOLLOW_SYSTEM
                    )
                },
            )
            DropdownMenuItem(
                modifier = Modifier,
                text = {
                    Text(
                        text = stringResource(R.string.light_mode),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
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
                        color = MaterialTheme.colorScheme.onPrimaryContainer
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