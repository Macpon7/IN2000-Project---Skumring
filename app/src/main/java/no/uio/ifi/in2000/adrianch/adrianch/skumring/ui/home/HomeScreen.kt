package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.in2000.adrianch.adrianch.skumring.R
import no.uio.ifi.in2000.adrianch.adrianch.skumring.SkumringTopAppBar
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.NavigationDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.theme.SkumringTheme

object HomeDestination : NavigationDestination {
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
    ) { innerPadding ->
        Greeting(name = homeUiState.title, modifier = Modifier.padding(innerPadding))
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SkumringTheme {
        Greeting("Android")
    }
}