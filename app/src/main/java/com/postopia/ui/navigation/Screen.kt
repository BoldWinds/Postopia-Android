package com.postopia.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Space : Screen("space")
    object Post : Screen("post")
    object Message : Screen("message")
    object Profile : Screen("profile")
    object Auth : Screen("auth")
    object SpaceDetail : Screen("space_detail/{spaceId}") {
        fun createRoute(spaceId: Long) = "space_detail/$spaceId"
    }
}
