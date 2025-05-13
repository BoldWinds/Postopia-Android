package com.postopia.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.postopia.ui.home.HomeScreen
import com.postopia.ui.message.MessageScreen
import com.postopia.ui.post.PostScreen
import com.postopia.ui.profile.ProfileScreen
import com.postopia.ui.space.SpaceScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen(navController) }
        composable("space") { SpaceScreen(navController) }
        composable("post") { PostScreen(navController) }
        composable("message") { MessageScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
    }
}
