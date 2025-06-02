package com.postopia.ui.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.EditorController
import com.postopia.ui.model.SpaceDetailUiModel

@Composable
fun CreateScreen(
    viewModel: CreateViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(CreateEvent.SnackbarMessageShown)
        }
    }

    val selectedSpace = uiState.selectedSpace

    // 显示空间选择对话
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        SpaceSelectionDialog(
            spaces = uiState.spaces,
            onSpaceSelected = { space ->
                viewModel.handleEvent(CreateEvent.SelectSpace(space))
            },
            onDismiss = {
                showDialog = false
            },
            isLoadingMore = uiState.isLoadingMore,
            hasMore = uiState.hasMore,
            onLoadMore = {
                viewModel.handleEvent(CreateEvent.LoadMoreSpaces)
            }
        )
    }

    val state = rememberRichTextState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // 顶部导航栏
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {/*todo*/}) {
                Text(
                    text = "取消",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }

            TextButton(
                onClick = {viewModel.handleEvent(CreateEvent.CreatePost(state.toHtml()))},
                enabled = selectedSpace != null && uiState.title.isNotBlank()
            ) {
                Text(
                    text = "发布",
                    color = if (selectedSpace != null && uiState.title.isNotBlank()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    },
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 选择社区按钮
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDialog = true },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedSpace != null && selectedSpace.avatar.isNotEmpty()) {
                        AsyncImage(
                            model = selectedSpace.avatar,
                            contentDescription = "空间头像",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // 显示默认图标或首字母
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    if (selectedSpace != null)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.primaryContainer,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedSpace != null) {
                                Text(
                                    text = selectedSpace.name.firstOrNull()?.toString() ?: "",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = selectedSpace?.name ?: "选择空间",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "展开",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 标题输入
            Text(
                text = "Title",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            OutlinedTextField(
                value = uiState.title,
                onValueChange = { viewModel.handleEvent(CreateEvent.ModifyTitle(it)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = false,
                maxLines = 3
            )

            EditorController(state)
            RichTextEditor(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(8f),
                state = state,
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SpaceSelectionDialog(
    spaces: List<SpaceDetailUiModel>,
    onSpaceSelected: (SpaceDetailUiModel) -> Unit,
    onDismiss: () -> Unit,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    onLoadMore: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= spaces.size - 3 &&
                    hasMore &&
                    !isLoadingMore
                ) {
                    onLoadMore()
                }
            }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // 关闭按钮
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                        .clickable { onDismiss() }
                        .align(Alignment.TopStart),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "关闭",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // 空间列表
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp) // 为顶部按钮留出空间
                ) {
                    items(spaces) { space ->
                        SpaceItem(
                            space = space,
                            onClick = {
                                onSpaceSelected(space)
                                onDismiss()
                            }
                        )
                    }
                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpaceItem(
    space: SpaceDetailUiModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 空间头像
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                if (space.avatar.isNotEmpty()) {
                    AsyncImage(
                        model = space.avatar,
                        contentDescription = "空间头像",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // 显示首字母或默认图标
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = space.name.firstOrNull()?.toString() ?: "",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 空间信息
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = space.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = space.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "成员: ${space.memberCount}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "帖子: ${space.postCount}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

