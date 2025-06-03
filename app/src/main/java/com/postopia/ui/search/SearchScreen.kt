package com.postopia.ui.search

import android.R.attr.type
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.postopia.ui.model.SearchType

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    query: String,
    onBack: () -> Unit,
    navigateToRoute: (String) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(query) {
        viewModel.handleEvent(
            SearchEvent.NewSearch(
                query = query
            )
        )
    }

    val lazyListState = rememberLazyListState()
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= uiState.searchResults.size - 3 &&
                    uiState.hasMoreResults &&
                    !uiState.isLoadingResults) {
                    viewModel.handleEvent(SearchEvent.LoadMoreResults)
                }
            }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // todo显示搜索结果
    }

}