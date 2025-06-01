package com.postopia.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
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
        modifier = Modifier
            .height(40.dp)
            .shadow(2.dp, RoundedCornerShape(8.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.HowToVote,
            contentDescription = "投票",
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "投票",
            style = MaterialTheme.typography.labelLarge
        )
    }
}