package com.postopia.ui.space

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.postopia.data.model.SpaceInfo
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.SpaceCard

@Composable
fun SpaceScreen(
    viewModel: SpaceViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateToSpaceDetail: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(SpaceEvent.SnackbarMessageShown)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        sharedViewModel.setLoading(uiState.isLoading)
    }

    val popularSpaces = uiState.popularSpaces
    val userSpaces = uiState.userSpaces

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 推荐空间部分
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "热门空间",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            SpaceHorizontalList(
                spaces = popularSpaces,
                navigateToSpaceDetail = navigateToSpaceDetail,
                onJoinOrLeave = { spaceId, join ->
                    viewModel.handleEvent(SpaceEvent.JoinOrLeave(spaceId, join, true))
                },
                onLoadMore = {
                    viewModel.handleEvent(SpaceEvent.LoadMoreSpaces(true))
                },
                isLoadingMore = uiState.isLoadingMorePopularSpaces,
                hasMore = uiState.hasMorePopularSpaces,
            )
        }

        // 用户空间部分
        item {
            Text(
                text = "你的空间",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
        if (userSpaces.isNotEmpty()) {
            item {
                SpaceHorizontalList(
                    spaces = userSpaces,
                    navigateToSpaceDetail = navigateToSpaceDetail,
                    onJoinOrLeave = { spaceId, join ->
                        viewModel.handleEvent(SpaceEvent.JoinOrLeave(spaceId, join, false))
                    },
                    onLoadMore = {
                        viewModel.handleEvent(SpaceEvent.LoadMoreSpaces(false))
                    },
                    isLoadingMore = uiState.isLoadingMoreUserSpaces,
                    hasMore = uiState.hasMoreUserSpaces,
                )
            }
        } else {
            item {
                Text(
                    text = "您还没有加入任何空间，快去探索吧！",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SpaceHorizontalList(
    spaces: List<SpaceInfo>,
    navigateToSpaceDetail: (Long) -> Unit,
    onJoinOrLeave: (spaceId: Long, join: Boolean) -> Unit = { _, _ -> },
    onLoadMore: () -> Unit = {},
    isLoadingMore: Boolean = false,
    hasMore : Boolean,
) {
    // 将空间列表按两个一组分组
    val groupedSpaces = spaces.chunked(2)

    // 使用LazyListState来监控滚动状态
    val listState = rememberLazyListState()

    // 监控滚动并在接近末尾时触发加载更多
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= (groupedSpaces.size - 1) &&
                    !isLoadingMore &&
                    hasMore) {
                    onLoadMore()
                }
            }
    }

    LazyRow(
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 0.dp)
    ) {
        items(groupedSpaces) { spaceGroup ->
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.width(280.dp)
            ) {
                spaceGroup.forEach { spaceInfo ->
                    SpaceCard(
                        space = spaceInfo.space,
                        isMember = spaceInfo.isMember,
                        onSpaceClick = {
                            navigateToSpaceDetail(spaceInfo.space.id)
                        },
                        joinOrLeave = {
                            onJoinOrLeave(spaceInfo.space.id, spaceInfo.isMember)
                        }
                    )
                }
            }
        }

        // 显示加载中指示器
        if (isLoadingMore) {
            item {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(100.dp)
                        .height(150.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}