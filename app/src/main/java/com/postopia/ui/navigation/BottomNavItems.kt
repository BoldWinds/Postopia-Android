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
    BottomNavItem("Home", Icons.Default.Home, Screen.Home.route),
    BottomNavItem("Space", Icons.Default.Place, Screen.Space.route),
    BottomNavItem("Create", Icons.Default.Add, Screen.Create.route),
    BottomNavItem("Message", Icons.Default.Email, Screen.Message.route),
    BottomNavItem("Profile", Icons.Default.Person, Screen.Profile.route)
)