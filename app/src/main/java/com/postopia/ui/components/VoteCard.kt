package com.postopia.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.postopia.data.model.OpinionStatus
import com.postopia.ui.model.VoteDialogUiModel

/*
* To be deprecated in favor of VoteButton
*
* */
@Composable
fun VoteCard(
    voteModel: VoteDialogUiModel,
    onVote: (Long, Boolean) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { showDialog = true },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(voteModel.opinion == OpinionStatus.NIL) MaterialTheme.colorScheme.primaryContainer
                else    MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = voteModel.voteType.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if(voteModel.opinion == OpinionStatus.NIL) MaterialTheme.colorScheme.onPrimaryContainer
                else    MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }

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
}

