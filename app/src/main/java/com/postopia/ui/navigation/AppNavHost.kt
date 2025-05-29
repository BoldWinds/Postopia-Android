package com.postopia.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.postopia.ui.SharedViewModel
import com.postopia.ui.auth.AuthScreen
import com.postopia.ui.home.HomeScreen
import com.postopia.ui.message.MessageScreen
import com.postopia.ui.post.PostScreen
import com.postopia.ui.profile.ProfileScreen
import com.postopia.ui.space.SpaceDetailScreen
import com.postopia.ui.space.SpaceScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel
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
                HomeScreen(sharedViewModel = sharedViewModel)
        }
        composable(
            Screen.Space.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
                SpaceScreen(
                    sharedViewModel = sharedViewModel,
                    navigateToSpaceDetail = { spaceId ->
                        navController.navigate(Screen.SpaceDetail.createRoute(spaceId))
                    }
                )
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
                ProfileScreen(sharedViewModel = sharedViewModel)
        }
        composable(
            Screen.Auth.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            AuthScreen(
                sharedViewModel = sharedViewModel,
                navigateBack = {navController.popBackStack()},
                navigateToHome = {navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Auth.route) {
                        inclusive = true
                    }
                }}
            )
        }

        composable(
            route = Screen.SpaceDetail.route,
            arguments = listOf(
                navArgument("spaceId") { type = NavType.LongType }
            ),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val spaceId = backStackEntry.arguments?.getLong("spaceId") ?: 0L

            SpaceDetailScreen(
                spaceId = spaceId,
                sharedViewModel = sharedViewModel
            )
        }
    }
}
