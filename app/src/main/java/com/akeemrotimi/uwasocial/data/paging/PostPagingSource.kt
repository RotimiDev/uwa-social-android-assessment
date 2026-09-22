package com.akeemrotimi.uwasocial.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.akeemrotimi.uwasocial.data.local.PostDao
import com.akeemrotimi.uwasocial.data.mapper.toDomain
import com.akeemrotimi.uwasocial.data.mapper.toEntity
import com.akeemrotimi.uwasocial.data.remote.PostApiService
import com.akeemrotimi.uwasocial.domain.model.Post
import com.akeemrotimi.uwasocial.domain.util.NetworkMonitor
import java.io.IOException

class PostPagingSource(
    private val apiService: PostApiService,
    private val postDao: PostDao,
    private val networkMonitor: NetworkMonitor
) : PagingSource<Int, Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: STARTING_PAGE
        val pageSize = params.loadSize.coerceAtMost(PAGE_SIZE)

        val wasOffline = !networkMonitor.isOnline()

        return try {
            if (wasOffline) {
                loadFromCacheOrError(page)
            } else {
                val response = apiService.getPosts(page, pageSize)
                postDao.replacePage(page, response.posts.map { it.toEntity(page) })
                LoadResult.Page(
                    data = response.posts.map { it.toDomain() },
                    prevKey = if (page == STARTING_PAGE) null else page - 1,
                    nextKey = if (response.isLastPage) null else page + 1
                )
            }
        } catch (networkFailure: IOException) {
            loadFromCacheOrError(page, cause = networkFailure)
        } catch (unexpected: Exception) {
            LoadResult.Error(unexpected)
        }
    }

    private suspend fun loadFromCacheOrError(
        page: Int,
        cause: Throwable? = null
    ): LoadResult<Int, Post> {
        val cached = postDao.getPage(page)
        if (cached.isEmpty()) {
            return LoadResult.Error(
                cause ?: IOException("No cached posts available for page $page")
            )
        }
        return LoadResult.Page(
            data = cached.map { it.toDomain() },
            prevKey = if (page == STARTING_PAGE) null else page - 1,
            nextKey = page + 1
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    companion object {
        const val STARTING_PAGE = 1
        const val PAGE_SIZE = 10
    }
}
