package com.postopia.ui.message

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun MessageScreen(navController: NavController){
    Text("Message Screen",color= MaterialTheme.colorScheme.primary)
}
