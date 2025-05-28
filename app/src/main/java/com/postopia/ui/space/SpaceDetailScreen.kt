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
import com.postopia.data.model.SpacePart
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.ArrowBack
import com.postopia.utils.DateUtils

@Composable
fun SpaceDetailScreen(
    viewModel: SpaceViewModel = hiltViewModel(),
    sharedViewModel : SharedViewModel,
    spaceId: Long,
    navigateBack: () -> Unit
) {
    LaunchedEffect(spaceId) {
        viewModel.handleEvent(SpaceEvent.LoadSpaceDetail(spaceId))
    }

    val uiState by viewModel.uiState.collectAsState()
    val isMember = uiState.spaceInfo?.isMember
    val space = uiState.spaceInfo?.space

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(SpaceEvent.SnackbarMessageShown)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        uiState.isLoading.let { isLoading ->
            sharedViewModel.setLoading(isLoading)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ArrowBack { navigateBack() }
        SpaceDetailTopBar(
            spacePart = space,
            onJoinClick = {
                viewModel.handleEvent(SpaceEvent.JoinOrLeave(spaceId, join = isMember == true))
            }
        )
        // 空间详情信息
        SpaceDetailInfo(spacePart = space)
        // TODO 空间帖子列表
        Text("TODO Space posts here")
    }
}


@Composable
fun SpaceDetailTopBar(
    spacePart: SpacePart?,
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
                if (spacePart != null) {
                    // 空间头像
                    AsyncImage(
                        model = spacePart.avatar,
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
                            text = spacePart.name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = formatMemberCount(spacePart.memberCount),
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
            }

            spacePart?.description?.let { description ->
                if (description.isNotEmpty()) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SpaceDetailInfo(spacePart: SpacePart?) {
    if (spacePart != null) {
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
                    value = spacePart.memberCount.toString(),
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
                    value = spacePart.postCount.toString(),
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
                    value = DateUtils.formatDate(spacePart.createdAt),
                    modifier = Modifier.weight(1f)
                )
            }

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