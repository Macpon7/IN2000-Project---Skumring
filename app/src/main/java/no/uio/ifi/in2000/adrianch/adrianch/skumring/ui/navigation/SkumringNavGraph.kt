package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites.FavoritesDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.favorites.FavoritesScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.home.HomeScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.maplist.MapListScreen
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage.MyPageDestination
import no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.mypage.MyPageScreen
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
            HomeScreen(navController = navController)
        }
        composable(route = MapListDestination.route) {
            MapListScreen(navController = navController)
        }
        composable(route = MyPageDestination.route) {
            MyPageScreen(navController = navController)
        }
        composable(route = FavoritesDestination.route){
            FavoritesScreen(navController = navController)
        }
        composable(
            route = PlaceInfoScreenDestination.route,
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
                )
            ) {backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id")
            if (id != null) {
                PlaceInfoScreen(
                    navController = navController,
                    id = id
                )
            }
        }
    }
}