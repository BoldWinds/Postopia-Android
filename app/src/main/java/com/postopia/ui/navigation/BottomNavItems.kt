package com.postopia.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)

val bottomNavItems = listOf(
    BottomNavItem("首页", Icons.Default.Home, Screen.Home.route),
    BottomNavItem("空间", Icons.Default.Place, Screen.Space.route),
    BottomNavItem("创作", Icons.Default.Add, Screen.Create.route),
    BottomNavItem("消息", Icons.Default.Email, Screen.Message.route),
    BottomNavItem("我的", Icons.Default.Person, Screen.Profile.route)
)