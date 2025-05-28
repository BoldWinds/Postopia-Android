package com.postopia.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.PostList


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
){
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            sharedViewModel.showSnackbar(message)
            viewModel.handleEvent(HomeEvent.SnackbarMessageShown)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        uiState.isLoading.let { isLoading ->
            sharedViewModel.setLoading(isLoading)
        }
    }

    PostList(
        posts = uiState.spaceInfos,
        isLoadingMore = uiState.isLoadingMore,
        onLoadMore = {
            viewModel.handleEvent(HomeEvent.LoadMorePosts(uiState.page))
        },
        onPostClick = { spaceInfo ->
            // todo open post detail
        },
    )
}
