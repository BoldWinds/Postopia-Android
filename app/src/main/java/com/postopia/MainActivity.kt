package com.postopia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.postopia.ui.components.BottomNavItem
import com.postopia.ui.components.BottomNavigationBar
import com.postopia.ui.home.HomeScreen
import com.postopia.ui.post.PostScreen
import com.postopia.ui.profile.ProfileScreen
import com.postopia.ui.space.SpaceScreen
import com.postopia.ui.theme.PostopiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostopiaTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val screens = listOf(
        BottomNavItem("home", Icons.Default.Home, "Home"),
        BottomNavItem("post", Icons.Default.Add, "Post"),
        BottomNavItem("space", Icons.Default.Place, "Space"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(items = screens, navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(navController) }
            composable("post") { PostScreen(navController) }
            composable("space") { SpaceScreen(navController) }
            composable("profile") { ProfileScreen(navController) }
        }
    }
}
