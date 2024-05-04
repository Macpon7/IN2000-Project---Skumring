package no.uio.ifi.in2000.adrianch.adrianch.skumring.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Interface to describe the navigation destinations for the app
 */
interface NavigationDestination {
    /**
     * Unique icon for the destination
     * Used for the icon in SkumringButtonBar
     */
    val icon: ImageVector?


    /**
     * Unique title for the destination,
     * Used for the title in SkumringButtonBar
     */
    val buttonTitle : Int?

    /**
     * Unique name to define the path for a composable
     */
    val route: String

    /**
     * String resource id to that contains title to be displayed for the screen.
     */
    val titleRes: Int?
}
