package com.postopia.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingContainer(
    isLoading : Boolean,
    successContent: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 始终显示内容
        successContent()

        // 如果正在加载，显示加载动画覆盖在上面
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 1.0f)),
                contentAlignment = Alignment.Center
            ) {
                PulsingDotsLoader()
            }
        }
    }
}