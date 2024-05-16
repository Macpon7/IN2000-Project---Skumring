package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.sharedcomponents

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites.FavoritesDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage.MyPageDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo.PlaceInfoScreenDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.settings.SettingsScreenDestination


@Composable
fun SkumringBottomBar(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    // List with the screens, will be updated when we have more screens:
    val screens = listOf(
        HomeDestination, MapListDestination, FavoritesDestination, MyPageDestination
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.scrim,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        screens.forEach { screen ->

            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(screen.buttonTitle!!),
                        style = MaterialTheme.typography.bodyMedium
                    ) // Have to use StringResource to make the resource to String
                },
                icon = {
                    screen.icon?.let { Icon(imageVector = it, contentDescription = "") }
                },
                selected = currentRoute == screen.route,
                onClick = {
                    /*
                    If the current screen is PlaceInfo or Settings, we pop back one step in the stack before navigating
                    This stops us from getting sent back to PlaceInfo or Settings next time we click on Map or MyPage
                     */
                    if (currentRoute == PlaceInfoScreenDestination.route || currentRoute == SettingsScreenDestination.route) {
                        navController.popBackStack()
                    }
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.inversePrimary,

                    ),
            )
        }
    }
}
