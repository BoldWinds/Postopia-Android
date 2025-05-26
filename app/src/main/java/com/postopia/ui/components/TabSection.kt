package com.postopia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/*
    * Postopia
    * TabSection.kt
    * This file defines a composable function that creates a tab section
    * with three tabs: "Posts", "Comments", and "About".
    *
    * Used in the ProfileScreen to switch between different sections of the user's profile.
 */
@Composable
fun TabSection() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Posts", "Comments", "About")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF272729))
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFF272729),
            contentColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFF0079D3)
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTab == index) Color.White else Color.Gray
                        )
                    }
                )
            }
        }
    }
}