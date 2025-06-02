package com.postopia.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.CommentTree
import com.postopia.ui.components.LikeDislikeBar
import com.postopia.ui.components.ReplyDialog
import com.postopia.ui.components.VoteCard
import com.postopia.ui.model.PostDetailUiModel

@Composable
fun PostDetailScreen(
    viewModel: PostDetailViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    postId : Long,
    spaceId : Long,
    spaceName : String,
) {
    LaunchedEffect(postId) {
        viewModel.handleEvent(PostDetailEvent.LoadPostDetail(postId, spaceId, spaceName))
    }

    val uiState by viewModel.uiState.collectAsState()
    val postUiData = uiState.postDetail

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(PostDetailEvent.SnackbarMessageShown)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        sharedViewModel.setLoading(uiState.isLoading)
    }

    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.comments.size - 3 &&
                    uiState.hasMoreComments &&
                    !uiState.isLoadingComments) {
                    viewModel.handleEvent(PostDetailEvent.LoadComments)
                }
            }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 顶部信息栏
        item {
            PostContent(
                uiInfo = postUiData,
                updateOpinion = { isPositive -> viewModel.handleEvent(PostDetailEvent.UpdatePostOpinion(postId, spaceId, isPositive)) },
                cancelOpinion = { isPositive -> viewModel.handleEvent(PostDetailEvent.CancelPostOpinion(postId, isPositive)) },
                onReplyClick = { viewModel.handleEvent(PostDetailEvent.ShowReplyBox(null)) }
            )
        }
        if(uiState.vote != null){
            item{
                VoteCard(
                    voteModel = uiState.vote!!,
                    onVote = { voteId, isPositive ->
                        viewModel.handleEvent(PostDetailEvent.VoteOpinion(voteId, isPositive)) },)
            }
        }
        items(
            items = uiState.comments,
            key = { it.id }
        ) { comment ->
            CommentTree(
                comment = comment,
                voteMap = uiState.commentVotes,
                onVote = { voteId, isPositive ->
                    viewModel.handleEvent(PostDetailEvent.CommentVoteOpinion(comment.id, voteId, isPositive)) },
                onCommentClick = { comment -> viewModel.handleEvent(PostDetailEvent.ShowReplyBox(comment)) },
                onUserClick = { /* TODO 点击用户 */ },
                onUpdateOpinion = { commentId, isPositive -> viewModel.handleEvent(PostDetailEvent.UpdateCommentOpinion(commentId, spaceId, isPositive)) },
                onCancelOpinion = { commentId, isPositive -> viewModel.handleEvent(PostDetailEvent.CancelCommentOpinion(commentId, isPositive)) },
                onReplyClick = { commentId -> viewModel.handleEvent(PostDetailEvent.ShowReplyBox(comment)) }
            )
        }
    }

    ReplyDialog(
        isVisible = uiState.isReplyBoxVisible,
        replyToUser = uiState.replyToComment?.username ?: uiState.postDetail.nickname,
        replyToTime = uiState.replyToComment?.createdAt ?: uiState.postDetail.createdAt,
        replyToContent = uiState.replyToComment?.content ?: uiState.postDetail.content,
        onSendReply = { content ->
            viewModel.handleEvent(PostDetailEvent.SendReply(content))
        },
        onDismiss = {
            viewModel.handleEvent(PostDetailEvent.HideReplyBox)
        }
    )
}

@Composable
fun PostContent(
    uiInfo: PostDetailUiModel,
    updateOpinion: (Boolean) -> Unit,
    cancelOpinion: (Boolean) -> Unit,
    onReplyClick: () -> Unit,
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .clickable{onReplyClick}
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 用户信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(uiInfo.avatar)
                        .crossfade(true)
                        .build(),
                    contentDescription = "用户头像",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { /* todo 访问用户界面 */ },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = uiInfo.nickname,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = uiInfo.spaceName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium,
                    )
                }
                // 存档状态
                if (uiInfo.isArchived) {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = "已归档",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 帖子标题
            Text(
                text = uiInfo.subject,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 帖子内容
            Text(
                text = AnnotatedString.fromHtml(uiInfo.content),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            Text(
                text = uiInfo.createdAt,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            //Spacer(modifier = Modifier.height(16.dp))

            // 操作按钮行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 投票按钮
                LikeDislikeBar(
                    opinion = uiInfo.opinionStatus,
                    positiveCount = uiInfo.positiveCount,
                    negativeCount = uiInfo.negativeCount,
                    size = 20.dp,
                    updateOpinion = { isPositive ->
                        updateOpinion(isPositive)
                    },
                    cancelOpinion = { isPositive ->
                        cancelOpinion(isPositive)
                    }
                )
                // 评论按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onReplyClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = "评论",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = uiInfo.commentCount.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // 空占位符保持布局平衡
                Spacer(modifier = Modifier.width(1.dp))
            }
        }
    }
}

