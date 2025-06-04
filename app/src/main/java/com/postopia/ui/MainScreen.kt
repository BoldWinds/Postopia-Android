package com.postopia.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.postopia.ui.components.BottomNavigationBar
import com.postopia.ui.components.LoadingContainer
import com.postopia.ui.components.TopBar
import com.postopia.ui.navigation.AppNavHost
import com.postopia.ui.navigation.Screen
import com.postopia.ui.navigation.bottomNavItems

@Composable
fun MainScreen(
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val currentRoute = navBackStackEntry?.destination?.route
    val hideBarsRoutes = listOf("auth")
    // 延迟更新 shouldShowBars，避免在内容切换前就显示栏位
    var shouldShowBars by remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        snapshotFlow { currentRoute }.collect { route ->
            shouldShowBars = route?.let { currentRoute ->
                hideBarsRoutes.none { hiddenRoute -> currentRoute.contains(hiddenRoute) }
            } == true
        }
    }

    // 监听 Snackbar 消息
    LaunchedEffect(sharedViewModel.snackbarMessage) {
        sharedViewModel.snackbarMessage.collect { message ->
            message?.let {
                snackbarHostState.showSnackbar(it)
                sharedViewModel.snackbarMessageShown()
            }
        }
    }

    // 监听导航到认证界面的事件
    LaunchedEffect(sharedViewModel.navigateToAuth) {
        sharedViewModel.navigateToAuth.collect { shouldNavigate ->
            if (shouldNavigate) {
                navController.navigate(Screen.Auth.route)
                sharedViewModel.onAuthNavigated()
            }
        }
    }

    val isLoading by sharedViewModel.isLoading.collectAsState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState){ data ->
                Snackbar(
                    modifier = Modifier.padding(12.dp),
                    snackbarData = data,
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    shape = RoundedCornerShape(8.dp),
                )
            }},
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if(shouldShowBars) TopBar(
                route = navController.currentDestination?.route ?: "",
                onNavigate = {route -> navController.navigate(route)},
                onBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    } else {
                        // 其他情况返回首页
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                }
            )
        },
        bottomBar = {
            if(shouldShowBars) BottomNavigationBar(items = bottomNavItems, navController = navController)
        }
    ) { innerPadding ->
        LoadingContainer(isLoading = isLoading) {
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                sharedViewModel = sharedViewModel
            )
        }
    }
}
