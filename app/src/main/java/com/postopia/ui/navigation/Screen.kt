package com.postopia.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Space : Screen("space")
    object Create : Screen("create")
    object Message : Screen("message")
    object Profile : Screen("profile")
    object Auth : Screen("auth")
    object SpaceDetail : Screen("space_detail/{spaceId}") {
        fun createRoute(spaceId: Long) = "space_detail/$spaceId"
    }
    object PostDetail : Screen("post_detail/{postId}/{spaceId}/{spaceName}") {
        fun createRoute(postId: Long, spaceId: Long, spaceName: String) =
            "post_detail/$postId/$spaceId/$spaceName"
    }
    object Search : Screen("search/{searchType}/{query}") {
        fun createRoute(searchType: String, query: String) =
            "search/$searchType/$query"
    }
}
