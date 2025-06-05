package com.postopia.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.postopia.data.model.VoteType
import com.postopia.ui.model.CommentTreeNodeUiModel
import com.postopia.ui.model.VoteDialogUiModel
import com.postopia.utils.DateUtils


@Composable
fun CommentTree(
    comment: CommentTreeNodeUiModel,
    voteMap: Map<Long, VoteDialogUiModel>,
    onVote: (Long, Boolean) -> Unit,
    onCommentClick: (CommentTreeNodeUiModel) -> Unit,
    onUserClick: (CommentTreeNodeUiModel) -> Unit,
    onUpdateOpinion: (Long, Boolean) -> Unit,
    onCancelOpinion: (Long, Boolean) -> Unit,
    onReplyClick: (Long) -> Unit,
    onCreateVote: (VoteType) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Top
    ) {
        CommentItem(
            comment = comment,
            vote = voteMap[comment.id],
            onVote = onVote,
            onCommentClick = onCommentClick,
            onCancelOpinion = onCancelOpinion,
            onUpdateOpinion = onUpdateOpinion,
            onReplyClick = onReplyClick,
            onCreateVote = onCreateVote,
        )

        // Render children with indentation
        if (comment.children.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(start = (comment.depth * 12).dp)
            ) {
                comment.children.forEach { childComment ->
                    Spacer(modifier = Modifier.height(4.dp))
                    CommentTree(
                        comment = childComment,
                        voteMap = voteMap,
                        onVote = onVote,
                        onCommentClick = onCommentClick,
                        onUserClick = onUserClick,
                        onCancelOpinion = onCancelOpinion,
                        onUpdateOpinion = onUpdateOpinion,
                        onReplyClick = onReplyClick,
                        onCreateVote = onCreateVote,
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(
    comment: CommentTreeNodeUiModel,
    vote: VoteDialogUiModel?,
    onVote: (Long, Boolean) -> Unit,
    onCommentClick: (CommentTreeNodeUiModel) -> Unit,
    onUpdateOpinion: (Long, Boolean) -> Unit,
    onCancelOpinion: (Long, Boolean) -> Unit,
    onReplyClick: (Long) -> Unit,
    onCreateVote: (VoteType) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCommentClick(comment) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { }
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(comment.avatarUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = comment.nickname,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                            )

                            if (comment.isPinned) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = "Pinned",
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Text(
                            text = "u/${comment.username}",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                if(vote == null){
                    VoteMenu(isPost = false, isArchivedOrPinned = comment.isPinned, onVote = onCreateVote)
                }else{
                    VoteButton(voteModel = vote, onVote = onVote)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comment content
            Text(
                text = AnnotatedString.fromHtml(comment.content),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左侧部分
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LikeDislikeBar(
                        positiveCount = comment.positiveCount,
                        negativeCount = comment.negativeCount,
                        opinion = comment.opinion,
                        size = 16.dp,
                        cancelOpinion = { isPositive -> onCancelOpinion(comment.id, isPositive) },
                        updateOpinion = { isPositive -> onUpdateOpinion(comment.id, isPositive) },
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChatBubbleOutline,
                            contentDescription = "评论",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = comment.children.size.toString(),
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
                Text(
                    text = "回复于${DateUtils.formatDate(comment.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}