package com.akeemrotimi.uwasocial.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.akeemrotimi.uwasocial.R
import com.akeemrotimi.uwasocial.domain.model.Post
import com.akeemrotimi.uwasocial.ui.component.AppendErrorRow
import com.akeemrotimi.uwasocial.ui.component.AppendLoadingRow
import com.akeemrotimi.uwasocial.ui.component.EmptyFeedState
import com.akeemrotimi.uwasocial.ui.component.ErrorFeedState
import com.akeemrotimi.uwasocial.ui.component.FullScreenLoading
import com.akeemrotimi.uwasocial.ui.component.OfflineFeedState
import com.akeemrotimi.uwasocial.ui.component.PostItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(viewModel: FeedViewModel = hiltViewModel()) {
    val items = viewModel.postsWithLikeState.collectAsLazyPagingItems()
    val refreshState = items.loadState.refresh
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.feed_title))
                }
            )
        }
    ) { innerPadding ->

        when {
            refreshState is LoadState.Loading && items.itemCount == 0 -> {
                FullScreenLoading(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            refreshState is LoadState.Error && items.itemCount == 0 -> {
                if (!isOnline) {
                    OfflineFeedState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onRetry = { items.retry() }
                    )
                } else {
                    ErrorFeedState(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        message = refreshState.error.message
                            ?: stringResource(R.string.feed_default_error),
                        onRetry = { items.retry() }
                    )
                }
            }

            refreshState is LoadState.NotLoading && items.itemCount == 0 -> {
                EmptyFeedState(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding
                ) {
                    items(count = items.itemCount) { index ->
                        val post: Post? = items[index]

                        if (post != null) {
                            PostItem(
                                post = post,
                                onLikeClick = viewModel::toggleLike
                            )
                            HorizontalDivider()
                        }
                    }

                    when (items.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                AppendLoadingRow()
                            }
                        }

                        is LoadState.Error -> {
                            item {
                                AppendErrorRow(
                                    onRetry = { items.retry() }
                                )
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}
