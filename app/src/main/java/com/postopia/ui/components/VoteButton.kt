package com.postopia.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.postopia.data.model.OpinionStatus
import com.postopia.ui.model.VoteDialogUiModel

@Composable
fun VoteButton(
    voteModel: VoteDialogUiModel,
    onVote: (Long, Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        // Show the vote dialog
        VoteDialog(
            voteModel = voteModel,
            onDismiss = { showDialog = false },
            onVote = { id, isPositive ->
                onVote(id, isPositive)
                showDialog = false
            }
        )
    }

    Button(
        onClick = { showDialog = true },
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if(voteModel.opinion == OpinionStatus.NIL) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceVariant,
            contentColor =
                if(voteModel.opinion == OpinionStatus.NIL) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = if(voteModel.opinion == OpinionStatus.NIL) "投票" else "已投票",
            style = MaterialTheme.typography.labelLarge
        )
    }
}