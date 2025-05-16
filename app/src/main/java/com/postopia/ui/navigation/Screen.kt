package com.postopia.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Space : Screen("space")
    object Post : Screen("post")
    object Message : Screen("message")
    object Profile : Screen("profile")
    object Auth : Screen("auth")
}
