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
import com.postopia.ui.create.CreateScreen
import com.postopia.ui.home.HomeScreen
import com.postopia.ui.message.MessageScreen
import com.postopia.ui.post.PostDetailScreen
import com.postopia.ui.profile.ProfileScreen
import com.postopia.ui.search.SearchScreen
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
                HomeScreen(
                    sharedViewModel = sharedViewModel,
                    navigateToPostDetail = { postId, spaceId, spaceName ->
                        navController.navigate(Screen.PostDetail.createRoute(postId, spaceId, spaceName))
                    },
                )
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
            Screen.Create.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
            CreateScreen(
                sharedViewModel = sharedViewModel,
            )
        }
        composable(
            Screen.Message.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
                MessageScreen(
                    sharedViewModel = sharedViewModel,
                )
        }
        composable(
            Screen.Profile.route,
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) {
                ProfileScreen(
                    sharedViewModel = sharedViewModel,
                    navigateToPostDetail = { postId, spaceId, spaceName ->
                        navController.navigate(Screen.PostDetail.createRoute(postId, spaceId, spaceName))
                    },
                )
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
                sharedViewModel = sharedViewModel,
                navigateToPostDetail = { postId, spaceId, spaceName ->
                    navController.navigate(Screen.PostDetail.createRoute(postId, spaceId, spaceName))
                },
            )
        }

        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(
                navArgument("postId") { type = NavType.LongType },
                navArgument("spaceId") { type = NavType.LongType },
                navArgument("spaceName") { type = NavType.StringType }
            ),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getLong("postId") ?: -1L
            val spaceId = backStackEntry.arguments?.getLong("spaceId") ?: -1L
            val spaceName = backStackEntry.arguments?.getString("spaceName") ?: ""

            PostDetailScreen(
                postId = postId,
                spaceId = spaceId,
                spaceName = spaceName,
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = Screen.Search.route,
            arguments = listOf(
                navArgument("searchType") { type = NavType.StringType },
                navArgument("query") { type = NavType.StringType }
            ),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(300)) }
        ) { backStackEntry ->
            val searchType = backStackEntry.arguments?.getString("searchType") ?: ""
            val query = backStackEntry.arguments?.getString("query") ?: ""

            SearchScreen(
                searchType = searchType,
                query = query,
                onBack = { navController.popBackStack() },
                navigateToRoute = { route->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
