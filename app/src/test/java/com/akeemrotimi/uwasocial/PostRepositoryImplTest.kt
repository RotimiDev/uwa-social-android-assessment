package com.akeemrotimi.uwasocial

import com.akeemrotimi.uwasocial.data.local.PostDao
import com.akeemrotimi.uwasocial.data.local.PostEntity
import com.akeemrotimi.uwasocial.data.remote.PostApiService
import com.akeemrotimi.uwasocial.domain.repository.PostRepositoryImpl
import com.akeemrotimi.uwasocial.domain.util.NetworkMonitor
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PostRepositoryImplTest {

    private val apiService = mockk<PostApiService>(relaxed = true)
    private val postDao = mockk<PostDao>(relaxed = true)
    private val networkMonitor = mockk<NetworkMonitor>(relaxed = true)

    private lateinit var repository: PostRepositoryImpl

    @Before
    fun setup() {
        repository = PostRepositoryImpl(
            apiService = apiService,
            postDao = postDao,
            networkMonitor = networkMonitor
        )
    }

    @Test
    fun `toggleLike likes post and increments count`() = runTest {
        val post = PostEntity(
            id = "1",
            userName = "John",
            userAvatarUrl = "",
            text = "Hello",
            location = null,
            timestampMillis = 0L,
            mediaUrl = null,
            isLikedByUser = false,
            likeCount = 5,
            commentCount = 2,
            page = 1
        )

        coEvery { postDao.getById("1") } returns post

        repository.toggleLike("1")

        coVerify {
            postDao.updateLike(
                postId = "1",
                isLiked = true,
                likeCount = 6
            )
        }
    }

    @Test
    fun `toggleLike unlikes post and decrements count`() = runTest {
        val post = PostEntity(
            id = "1",
            userName = "John",
            userAvatarUrl = "",
            text = "Hello",
            location = null,
            timestampMillis = 0L,
            mediaUrl = null,
            isLikedByUser = true,
            likeCount = 5,
            commentCount = 2,
            page = 1
        )

        coEvery { postDao.getById("1") } returns post

        repository.toggleLike("1")

        coVerify {
            postDao.updateLike(
                postId = "1",
                isLiked = false,
                likeCount = 4
            )
        }
    }
}
