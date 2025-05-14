package com.postopia.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.postopia.ui.components.BottomNavigationBar
import com.postopia.ui.components.TopBar
import com.postopia.ui.navigation.AppNavHost
import com.postopia.ui.navigation.bottomNavItems

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val hideBarsRoutes = listOf("register", "login")
    val shouldShowBars = currentDestination?.route !in hideBarsRoutes

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if(shouldShowBars) TopBar({}, {}, {navController.navigate("register")})
        },
        bottomBar = {
            if(shouldShowBars)  BottomNavigationBar(items = bottomNavItems, navController = navController)
        }
    ) { innerPadding ->
        AppNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
