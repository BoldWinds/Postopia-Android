package com.postopia.ui.space

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.PostList
import com.postopia.ui.model.SpaceDetailUiModel
import com.postopia.utils.DateUtils

@Composable
fun SpaceDetailScreen(
    viewModel: SpaceDetailViewModel = hiltViewModel(),
    sharedViewModel : SharedViewModel,
    spaceId: Long,
    navigateToPostDetail : (Long, Long, String) -> Unit
) {
    LaunchedEffect(spaceId) {
        viewModel.handleEvent(SpaceDetailEvent.LoadSpaceDetail(spaceId))
    }

    val uiState by viewModel.uiState.collectAsState()
    val spaceUiInfo = uiState.spaceInfo

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(SpaceDetailEvent.SnackbarMessageShown)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        sharedViewModel.setLoading(uiState.isLoading)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SpaceDetailTopBar(
            uiInfo = spaceUiInfo,
            onJoinClick = {
                viewModel.handleEvent(SpaceDetailEvent.JoinOrLeave(spaceId, join = spaceUiInfo.isMember == true))
            }
        )
        // 空间详情信息
        SpaceDetailInfo(uiInfo = spaceUiInfo)

        // TODO 空间帖子 与投票 的 tab
        PostList(
            posts = uiState.spacePosts,
            isLoadingMore = uiState.isLoadingMore,
            hasMore = uiState.hasMore,
            onLoadMore = {
                viewModel.handleEvent(SpaceDetailEvent.LoadMorePosts)
            },
            onPostClick = { postId, spaceId, spaceName ->
                navigateToPostDetail(postId, spaceId, spaceName)
            },
        )
    }
}


@Composable
fun SpaceDetailTopBar(
    uiInfo: SpaceDetailUiModel,
    onJoinClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                    // 空间头像
                    AsyncImage(
                        model = uiInfo.avatar,
                        contentDescription = "Space Avatar",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // 空间信息
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = uiInfo.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatMemberCount(uiInfo.memberCount),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // 加入按钮
                    FilledTonalButton(
                        onClick = onJoinClick,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Join",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

            }


            if (uiInfo.description.isNotEmpty()) {
                Text(
                    text = uiInfo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

        }
    }
}

@Composable
fun SpaceDetailInfo(uiInfo: SpaceDetailUiModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        // 统计信息
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatisticItem(
                label = "Members",
                value = uiInfo.memberCount.toString(),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
            StatisticItem(
                label = "Posts",
                value = uiInfo.postCount.toString(),
                modifier = Modifier.weight(1f)
            )
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
                    .background(MaterialTheme.colorScheme.outline)
            )
            StatisticItem(
                label = "Created",
                value = DateUtils.formatDate(uiInfo.createdAt),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun StatisticItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}