package com.postopia.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

@Composable
fun UsernameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val colorScheme = MaterialTheme.colorScheme

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("用户名") },
        leadingIcon = { Icon(Icons.Default.Person, contentDescription = "用户名") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorScheme.primary,
            focusedLabelColor = colorScheme.primary,
            cursorColor = colorScheme.primary,
            errorBorderColor = colorScheme.error,
            errorLabelColor = colorScheme.error
        )
    )
}