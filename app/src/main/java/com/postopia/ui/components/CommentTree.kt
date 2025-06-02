package com.postopia.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Reply
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.postopia.ui.model.CommentTreeNodeUiModel
import com.postopia.ui.model.VoteDialogUiModel


@Composable
fun CommentTree(
    comment: CommentTreeNodeUiModel,
    voteMap: Map<Long, VoteDialogUiModel>,
    onVote: (Long, Boolean) -> Unit,
    onCommentClick: (CommentTreeNodeUiModel) -> Unit,
    onUserClick: (CommentTreeNodeUiModel) -> Unit,
    onUpdateOpinion: (Long, Boolean) -> Unit,
    onCancelOpinion: (Long, Boolean) -> Unit,
    onReplyClick: (Long) -> Unit
) {
    Column {
        CommentItem(
            comment = comment,
            vote = voteMap[comment.id],
            onVote = onVote,
            onCommentClick = onCommentClick,
            onCancelOpinion = onCancelOpinion,
            onUpdateOpinion = onUpdateOpinion,
            onReplyClick = onReplyClick
        )

        // Render children with indentation
        if (comment.children.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(start = (16 + comment.depth * 12).dp)
            ) {
                comment.children.forEach { childComment ->
                    Spacer(modifier = Modifier.height(8.dp))
                    CommentTree(
                        comment = childComment,
                        voteMap = voteMap,
                        onVote = onVote,
                        onCommentClick = onCommentClick,
                        onUserClick = onUserClick,
                        onCancelOpinion = onCancelOpinion,
                        onUpdateOpinion = onUpdateOpinion,
                        onReplyClick = onReplyClick
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
    onReplyClick: (Long) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCommentClick(comment) },
        colors = CardDefaults.cardColors(
            containerColor = if (comment.depth == 0)
                MaterialTheme.colorScheme.surface
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (comment.depth == 0) 2.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with user info and time
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
                                color = MaterialTheme.colorScheme.onSurface
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
                            text = "@${comment.username}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // 添加VoteButton，仅当vote不为null时显示
                if (vote != null) {
                    VoteButton(voteModel = vote, onVote = onVote)
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = comment.createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Comment content
            Text(
                text = AnnotatedString.fromHtml(comment.content),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Actions row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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

                    Spacer(modifier = Modifier.width(16.dp))

                    // Reply button
                    TextButton(
                        onClick = { onReplyClick(comment.id) },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Reply,
                            contentDescription = "Reply",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Reply",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Show reply count if has children
                if (comment.children.isNotEmpty()) {
                    Text(
                        text = "${comment.children.size} ${if (comment.children.size == 1) "reply" else "replies"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}