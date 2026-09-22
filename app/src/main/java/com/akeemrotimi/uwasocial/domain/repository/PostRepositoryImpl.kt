package com.akeemrotimi.uwasocial.domain.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.akeemrotimi.uwasocial.data.local.PostDao
import com.akeemrotimi.uwasocial.data.paging.PostPagingSource
import com.akeemrotimi.uwasocial.data.remote.PostApiService
import com.akeemrotimi.uwasocial.domain.model.Post
import com.akeemrotimi.uwasocial.domain.util.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepositoryImpl @Inject constructor(
    private val apiService: PostApiService,
    private val postDao: PostDao,
    private val networkMonitor: NetworkMonitor
) : PostRepository {

    override fun getPostsPaged(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = PostPagingSource.PAGE_SIZE,
                enablePlaceholders = false,
                initialLoadSize = PostPagingSource.PAGE_SIZE
            ),
            pagingSourceFactory = { PostPagingSource(apiService, postDao, networkMonitor) }
        ).flow
    }

    override suspend fun toggleLike(postId: String) {
        val cached = postDao.getById(postId) ?: return
        val nowLiked = !cached.isLikedByUser
        val newCount =
            if (nowLiked) cached.likeCount + 1 else (cached.likeCount - 1).coerceAtLeast(0)
        postDao.updateLike(postId, nowLiked, newCount)
    }
}
