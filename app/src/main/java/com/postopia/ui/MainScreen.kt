package com.postopia.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.postopia.ui.components.BottomNavigationBar
import com.postopia.ui.components.TopBar
import com.postopia.ui.navigation.AppNavHost
import com.postopia.ui.navigation.bottomNavItems

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopBar({}, {}, {})
        },
        bottomBar = {
            BottomNavigationBar(items = bottomNavItems, navController = navController)
        }
    ) { innerPadding ->
        AppNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
