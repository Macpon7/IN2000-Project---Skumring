package no.uio.ifi.in2000.adrianch.adrianch.skumring

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation.SkumringNavHost
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents.SkumringBottomBar

@Composable
@Preview
fun SkumringApp(navController: NavHostController = rememberNavController()) {
    SkumringNavHost(navController = navController)
    NavigationBottomBar(navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkumringTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

/**
 * This ButtonBar will have all the different buttons for the different screens
 */
@Composable
fun NavigationBottomBar(navController: NavHostController) {
    var buttonsVisible = remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            SkumringBottomBar(
                navController = navController,
                state = buttonsVisible, //Show which screen you are in
                modifier = Modifier
            )
        }) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            SkumringNavHost(navController = navController)
        }
    }
}