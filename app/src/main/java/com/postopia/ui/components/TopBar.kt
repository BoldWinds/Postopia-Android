package com.postopia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    route: String,
    onMenuClick: () -> Unit,
    onSearchClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    var isSearchMode by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    TopAppBar(
        title = {
            if (isSearchMode) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = {
                        Text(
                            "搜索...",
                            color = Color.Gray
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFF4500),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFFFF4500)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchText.isNotBlank()) {
                                onSearchClick(searchText)
                                isSearchMode = false
                                searchText = ""
                            }
                        }
                    )
                )
            } else {
                Text(
                    text = "Postopia",
                    color = Color(0xFFFF4500), // Reddit-like orange color
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            if (isSearchMode) {
                IconButton(
                    onClick = {
                        isSearchMode = false
                        searchText = ""
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            }
        },
        actions = {
            if (isSearchMode) {
                // 搜索模式下显示发送按钮
                IconButton(
                    onClick = {
                        if (searchText.isNotBlank()) {
                            onSearchClick(searchText)
                            isSearchMode = false
                            searchText = ""
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "搜索",
                        tint = if (searchText.isNotBlank()) Color(0xFFFF4500) else Color.Gray
                    )
                }
            } else {
                // 正常模式下显示搜索和个人资料按钮
                IconButton(
                    onClick = {
                        isSearchMode = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
                IconButton(onClick = { onProfileClick() }) {
                    // Custom profile icon with circle background similar to Reddit
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF57ABCB)) // Light blue background like in screenshot
                    ) {
                        // Green dot indicating online status
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Green)
                                .border(1.dp, Color(0xFF57ABCB), CircleShape)
                        )
                    }
                }
            }
        }
    )

    // 当进入搜索模式时自动聚焦到输入框
    LaunchedEffect(isSearchMode) {
        if (isSearchMode) {
            delay(100) // 小延迟确保UI已更新
            focusRequester.requestFocus()
        }
    }
}