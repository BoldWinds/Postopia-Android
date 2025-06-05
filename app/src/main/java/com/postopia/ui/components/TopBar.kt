package com.postopia.ui.components

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.postopia.R
import com.postopia.data.local.proto.userDataStore
import com.postopia.ui.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun TopBar(
    route: String,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
) {
    // 根据路由确定是否显示特定组件
    val isSearchScreen = route.startsWith("search/")
    val isDetailScreen = route.startsWith("space/") && !route.contains("post/")
    val isPostDetailScreen = route.contains("post/")
    val shouldShowBackOnly = isDetailScreen || isPostDetailScreen

    // 新增判断条件
    val isCreateScreen = route == Screen.Create.route
    val isMessageScreen = route == Screen.Message.route
    val isProfileScreen = route == Screen.Profile.route
    val shouldHideSearchButton = isCreateScreen || isMessageScreen || isProfileScreen
    val shouldHideAvatar = isProfileScreen

    var isSearchMode by remember { mutableStateOf(isSearchScreen) }
    var searchText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    val context = LocalContext.current
    val userAvatar = context.applicationContext.userDataStore.data
        .map { userProto ->
            userProto.avatar.takeIf { it.isNotEmpty() } ?: R.drawable.ic_launcher_background
        }
        .collectAsState(initial = R.drawable.ic_launcher_background)

    TopAppBar(
        title = {
            if (isSearchMode || isSearchScreen) {
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
                        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        cursorColor = MaterialTheme.colorScheme.tertiary
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchText.isNotBlank()) {
                                onNavigate(Screen.Search.createRoute(searchText))
                                if (!isSearchScreen) {
                                    isSearchMode = false
                                    searchText = ""
                                }
                            }
                        }
                    )
                )
            } else if (!shouldShowBackOnly) {
                Text(
                    text = "Postopia",
                    color = Color(0xFF114DD2),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = {
            if (isSearchMode || isSearchScreen) {
                IconButton(
                    onClick = {
                        if(!isSearchScreen) {
                            isSearchMode = false
                            searchText = ""
                        } else{
                            onBack()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "返回"
                    )
                }
            } else if (shouldShowBackOnly) {
                // 在SpaceDetail和PostDetail页面显示返回按钮
                IconButton(onClick = onBack) {
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
                            onNavigate(Screen.Search.createRoute(searchText))
                            if (!isSearchScreen) {
                                isSearchMode = false
                                searchText = ""
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "搜索",
                        tint = if (searchText.isNotBlank()) Color(0xFF114DD2) else Color.Gray
                    )
                }
            } else if (!shouldShowBackOnly) {
                // 在Home/Space/Message页面显示搜索和个人资料按钮
                if (!shouldHideSearchButton) {
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
                }
                if (!shouldHideAvatar) {
                    IconButton(onClick = {
                        if(userAvatar.value == R.drawable.ic_launcher_background){
                            onNavigate(Screen.Auth.route)
                        } else {
                            onNavigate(Screen.Profile.route)
                        }
                    }) {
                        AsyncImage(
                            model = userAvatar.value,
                            contentDescription = "用户头像",
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.ic_launcher_background),
                            error = painterResource(id = R.drawable.ic_launcher_background)
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
