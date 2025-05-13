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
    BottomNavItem("Home", Icons.Default.Home, "home"),
    BottomNavItem("Space", Icons.Default.Place, "space"),
    BottomNavItem("Create", Icons.Default.Add, "post"),
    BottomNavItem("Message", Icons.Default.Email, "message"),
    BottomNavItem("Profile", Icons.Default.Person, "profile")
)