package com.postopia.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.postopia.data.model.VoteType

@Composable
fun VoteMenu(
    isPost: Boolean = false,
    isArchivedOrPinned : Boolean,
    onVote: (VoteType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(contentAlignment = Alignment.Center) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "更多选项"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            val menuItems = if (isPost) {
                listOf(
                    if (isArchivedOrPinned) VoteType.UNARCHIVE_POST else VoteType.ARCHIVE_POST,
                    VoteType.DELETE_POST
                )
            } else {
                listOf(
                    if (isArchivedOrPinned) VoteType.UNPIN_COMMENT else VoteType.PIN_COMMENT,
                    VoteType.DELETE_COMMENT
                )
            }

            menuItems.forEach { voteType ->
                DropdownMenuItem(
                    text = { Text(voteType.toString()) },
                    onClick = {
                        onVote(voteType)
                        expanded = false
                    }
                )
            }
        }
    }
}