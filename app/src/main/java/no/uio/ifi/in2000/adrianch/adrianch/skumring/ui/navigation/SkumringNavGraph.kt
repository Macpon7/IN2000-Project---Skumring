package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo.PlaceInfoScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.placeinfo.PlaceInfoScreenDestination

@Composable
fun SkumringNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen()
        }
        composable(route = MapListDestination.route) {
            MapListScreen(navController = navController)
        }
        composable(route = PlaceInfoScreenDestination.route) {
            PlaceInfoScreen(navController = navController)
        }
    }
}