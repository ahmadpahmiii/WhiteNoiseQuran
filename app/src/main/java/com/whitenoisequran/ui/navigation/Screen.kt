package com.whitenoisequran.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Download : Screen("download/{reciterId}") {
        fun createRoute(reciterId: Int): String = "download/$reciterId"
    }
    object Main : Screen("main")
}
