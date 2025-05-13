package com.postopia.ui.home


import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun HomeScreen(navController: NavController){
    Text("Home Screen",color= MaterialTheme.colorScheme.primary)
    DebugColorButton()
}

@Composable
fun DebugColorButton() {
    val color = MaterialTheme.colorScheme.primary
    Button(onClick = {
        Log.d("DynamicColorTest", "Primary color: $color")
    }) {
        Text("打印主色")
    }
}
