package com.postopia.ui.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.postopia.ui.SharedViewModel
import com.postopia.ui.components.PostList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    navigateToPostDetail : (Long, Long) -> Unit
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

    PullToRefreshBox(
        isRefreshing = uiState.isRefreshing,
        onRefresh = {viewModel.handleEvent(HomeEvent.Refresh)},
    ) {
        PostList(
            posts = uiState.spaceInfos,
            isLoadingMore = uiState.isLoadingMore,
            hasMore = uiState.hasMore,
            onLoadMore = {
                viewModel.handleEvent(HomeEvent.LoadMorePosts)
            },
            onPostClick = { spaceId, postId ->
                navigateToPostDetail(spaceId, postId)
            },
        )
    }

}

