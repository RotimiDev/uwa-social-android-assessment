package com.akeemrotimi.uwasocial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.akeemrotimi.uwasocial.domain.model.Post
import com.akeemrotimi.uwasocial.domain.repository.PostRepository
import com.akeemrotimi.uwasocial.domain.util.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class LikeOverride(val isLiked: Boolean, val likeCount: Int)

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val repository: PostRepository,
    networkMonitor: NetworkMonitor
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkMonitor.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), networkMonitor.isOnline())

    private val likeOverrides = MutableStateFlow<Map<String, LikeOverride>>(emptyMap())

    private val pagedPosts: Flow<PagingData<Post>> =
        repository.getPostsPaged().cachedIn(viewModelScope)

    val postsWithLikeState: Flow<PagingData<Post>> =
        combine(pagedPosts, likeOverrides) { paging, overrides ->
            if (overrides.isEmpty()) paging else paging.map { post -> post.withOverride(overrides) }
        }

    private fun Post.withOverride(overrides: Map<String, LikeOverride>): Post {
        val override = overrides[id] ?: return this
        return copy(isLikedByUser = override.isLiked, likeCount = override.likeCount)
    }

    fun toggleLike(post: Post) {
        val current = likeOverrides.value[post.id]
        val currentlyLiked = current?.isLiked ?: post.isLikedByUser
        val currentCount = current?.likeCount ?: post.likeCount

        val newLiked = !currentlyLiked
        val newCount = if (newLiked) currentCount + 1 else (currentCount - 1).coerceAtLeast(0)

        likeOverrides.value += (post.id to LikeOverride(newLiked, newCount))

        viewModelScope.launch {
            repository.toggleLike(post.id)
        }
    }
}
