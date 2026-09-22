package com.akeemrotimi.uwasocial.domain.repository

import androidx.paging.PagingData
import com.akeemrotimi.uwasocial.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPostsPaged(): Flow<PagingData<Post>>
    suspend fun toggleLike(postId: String)
}
