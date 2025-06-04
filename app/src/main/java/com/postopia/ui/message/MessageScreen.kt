package com.postopia.ui.message

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.postopia.ui.SharedViewModel
import com.postopia.ui.model.MessageCardUiModel
import com.postopia.utils.DateUtils

@Composable
fun MessageScreen(
    viewModel: MessageViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    onNavigate: (String)-> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(MessageEvent.SnackbarMessageShown)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        uiState.isLoading.let { isLoading ->
            sharedViewModel.setLoading(isLoading)
        }
    }

    // 检测是否需要加载更多消息
    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.messages.size - 3 &&
                    uiState.hasMoreMessages &&
                    !uiState.isLoadingMessages
                ) {
                    viewModel.handleEvent(MessageEvent.LoadMessages)
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.isSelectionMode) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
            )
        ){
            if (uiState.isSelectionMode){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.handleEvent(MessageEvent.ExitSelectionMode) }) {
                            Icon(Icons.Default.Close, "退出选择模式")
                        }
                        Text("已选择 ${uiState.selectedMessages.size} 条消息")
                    }

                    Row {
                        // 全选/取消全选
                        IconButton(
                            onClick = {
                                // 判断是否已全选
                                if (uiState.selectedMessages.size == uiState.messages.size) {
                                    viewModel.handleEvent(MessageEvent.DeselectAllMessages)
                                } else {
                                    viewModel.handleEvent(MessageEvent.SelectAllMessages)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (uiState.selectedMessages.size == uiState.messages.size && uiState.messages.isNotEmpty())
                                    Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                                contentDescription = "全选"
                            )
                        }

                        // 标记为已读
                        IconButton(
                            onClick = {
                                if (uiState.selectedMessages.isNotEmpty()) {
                                    viewModel.handleEvent(MessageEvent.ReadMessage(uiState.selectedMessages))
                                    viewModel.handleEvent(MessageEvent.ExitSelectionMode)
                                }
                            },
                            enabled = uiState.selectedMessages.isNotEmpty()
                        ) {
                            Icon(Icons.Default.MarkEmailRead, "标记为已读")
                        }

                        // 删除选中的消息
                        IconButton(
                            onClick = {
                                if (uiState.selectedMessages.isNotEmpty()) {
                                    viewModel.handleEvent(MessageEvent.DeleteMessage(uiState.selectedMessages))
                                    viewModel.handleEvent(MessageEvent.ExitSelectionMode)
                                }
                            },
                            enabled = uiState.selectedMessages.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                "删除选中",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
        // 消息列表
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            if (uiState.messages.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "暂无消息",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(
                        count = uiState.messages.size,
                        key = { index -> uiState.messages[index].id }
                    ) { index ->
                        MessageCard(
                            message = uiState.messages[index],
                            isSelected = uiState.selectedMessages.contains(uiState.messages[index].id),
                            isSelectionMode = uiState.isSelectionMode,
                            onMarkAsRead = { messageId ->
                                viewModel.handleEvent(MessageEvent.ReadMessage(listOf(messageId)))
                            },
                            onDelete = { messageId ->
                                viewModel.handleEvent(MessageEvent.DeleteMessage(listOf(messageId)))
                            },
                            onSelect = { messageId, selected ->
                                viewModel.handleEvent(
                                    if (selected) MessageEvent.SelectMessage(messageId)
                                    else MessageEvent.DeselectMessage(messageId)
                                )
                            },
                            onLongClick = { messageId ->
                                viewModel.handleEvent(MessageEvent.EnterSelectionMode(messageId))
                            },
                            onNavigate = onNavigate,
                        )
                    }

                    if (uiState.isLoadingMessages) {
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
fun MessageCard(
    message: MessageCardUiModel,
    isSelected: Boolean,
    isSelectionMode: Boolean,
    onMarkAsRead: (Long) -> Unit,
    onDelete: (Long) -> Unit,
    onSelect: (Long, Boolean) -> Unit,
    onLongClick: (Long) -> Unit,
    onNavigate: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .combinedClickable(
                onClick = {
                    if(!message.isRead) onMarkAsRead(message.id)
                    if(message.route.isNotEmpty()) onNavigate(message.route)
                },
                onLongClick = {
                    if (!isSelectionMode) {
                        onLongClick(message.id)
                    }
                }
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                message.isRead -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // 选择模式下的复选框
            if (isSelectionMode) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onSelect(message.id, it) },
                    modifier = Modifier.align(Alignment.Top)
                )
                Spacer(modifier = Modifier.width(12.dp))
            } else {
                // 未读状态指示器
                if (!message.isRead) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .align(Alignment.Top)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                } else {
                    Spacer(modifier = Modifier.width(20.dp))
                }
            }

            // 消息内容区域
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // 消息内容
                Text(
                    text = AnnotatedString.fromHtml(message.content),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (message.isRead) FontWeight.Normal else FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 时间戳
                Text(
                    text = DateUtils.formatDate(message.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            // 操作按钮（非选择模式下显示）
            if (!isSelectionMode) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 标记已读/未读按钮
                    IconButton(
                        onClick = { onMarkAsRead(message.id) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (message.isRead) Icons.Default.MarkEmailUnread else Icons.Default.MarkEmailRead,
                            contentDescription = if (message.isRead) "标记为未读" else "标记为已读",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // 删除按钮
                    IconButton(
                        onClick = { onDelete(message.id) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "删除消息",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

