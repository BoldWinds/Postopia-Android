package com.postopia.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.postopia.data.model.OpinionStatus

@Composable
fun LikeDislikeBar(
    opinion : OpinionStatus,
    positiveCount : Long,
    negativeCount : Long,
    size : Dp = 18.dp,
    cancelOpinion : (Boolean) -> Unit = {},
    updateOpinion : (Boolean) -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 点赞按钮
        IconButton(
            onClick = {
                when (opinion) {
                    OpinionStatus.POSITIVE -> {
                        cancelOpinion(true)
                    }
                    OpinionStatus.NEGATIVE -> {
                        cancelOpinion(false)
                        updateOpinion(true)
                    }
                    OpinionStatus.NIL -> {
                        updateOpinion(true)
                    }
                }
            },
            modifier = Modifier.size(size + 4.dp)
        ) {
            Icon(
                imageVector = if (opinion == OpinionStatus.POSITIVE) Icons.Filled.ThumbUp
                else Icons.Outlined.ThumbUp,
                contentDescription = "点赞",
                modifier = Modifier.size(size)
            )
        }

        // 计数显示
        Text(
            text = "${positiveCount - negativeCount}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        // 点踩按钮
        IconButton(
            onClick = {
                when (opinion) {
                    OpinionStatus.POSITIVE -> {
                        cancelOpinion(true)
                        updateOpinion(false)
                    }
                    OpinionStatus.NEGATIVE -> {
                        cancelOpinion(false)
                    }
                    OpinionStatus.NIL -> {
                        updateOpinion(false)
                    }
                }
            },
            modifier = Modifier.size(size + 4.dp)
        ) {
            Icon(
                imageVector = if (opinion == OpinionStatus.NEGATIVE) Icons.Filled.ThumbDown
                else Icons.Outlined.ThumbDown,
                contentDescription = "点踩",
                modifier = Modifier.size(size)
            )
        }
    }
}
