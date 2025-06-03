package com.postopia.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Space : Screen("space")
    object Create : Screen("create")
    object Message : Screen("message")
    object Profile : Screen("profile")
    object Auth : Screen("auth")
    object SpaceDetail : Screen("space/{spaceId}") {
        fun createRoute(spaceId: Long) = "space/$spaceId"
    }
    object PostDetail : Screen("space/{spaceId}/post/{postId}") {
        fun createRoute(postId: Long, spaceId: Long) =
            "space/$spaceId/post/$postId"
    }
    object Search : Screen("search/{query}") {
        fun createRoute(query: String) =
            "search/$query"
    }
}
