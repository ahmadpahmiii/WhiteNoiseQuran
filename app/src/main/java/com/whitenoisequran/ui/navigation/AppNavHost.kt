package com.whitenoisequran.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.whitenoisequran.ui.download.DownloadScreen
import com.whitenoisequran.ui.main.MainScreen
import com.whitenoisequran.ui.onboarding.OnboardingScreen

@Composable
fun AppNavHost(
    startDestination: String,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onNavigateToDownload = { reciterId ->
                    navController.navigate(Screen.Download.createRoute(reciterId))
                },
                // "Later": listen online now; the Surah Index offers the download any time
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Download.route,
            arguments = listOf(
                navArgument("reciterId") { type = NavType.IntType }
            )
        ) {
            DownloadScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMain = {
                    // Back to the player when it's underneath (keeps its state); after onboarding, replace the flow with it
                    if (!navController.popBackStack(Screen.Main.route, inclusive = false)) {
                        navController.navigate(Screen.Main.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToDownload = { reciterId ->
                    navController.navigate(Screen.Download.createRoute(reciterId))
                }
            )
        }
    }
}
