package com.postopia.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.postopia.ui.auth.RegisterScreen
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
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(
            Screen.Home.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
                HomeScreen()
        }
        composable(
            Screen.Space.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
                SpaceScreen()
        }
        composable(
            Screen.Post.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
                PostScreen()
        }
        composable(
            Screen.Message.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
                MessageScreen()
        }
        composable(
            Screen.Profile.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
                ProfileScreen()
        }
        composable(
            Screen.Register.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            RegisterScreen(
                navigateToLogin = {},
                navigateBack = {navController.popBackStack()}
            )
        }
    }
}
