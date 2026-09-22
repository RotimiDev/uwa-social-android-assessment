package com.akeemrotimi.uwasocial

import android.util.Log
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.akeemrotimi.uwasocial.domain.model.Post
import com.akeemrotimi.uwasocial.domain.repository.PostRepository
import com.akeemrotimi.uwasocial.domain.util.NetworkMonitor
import com.akeemrotimi.uwasocial.ui.FeedViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<PostRepository>(relaxed = true)
    private val networkMonitor = mockk<NetworkMonitor>(relaxed = true)

    private lateinit var viewModel: FeedViewModel

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.isLoggable(any(), any()) } returns false
        every { networkMonitor.observe() } returns flowOf(true)
        every { networkMonitor.isOnline() } returns true
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `toggleLike updates post like state`() = runTest {
        val post = Post(
            id = "1",
            userName = "John",
            userAvatarUrl = "",
            text = "Hello",
            location = null,
            timestampMillis = 0L,
            mediaUrl = null,
            isLikedByUser = false,
            likeCount = 5,
            commentCount = 2
        )

        every {
            repository.getPostsPaged()
        } returns flowOf(
            PagingData.from(
                data = listOf(post),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true)
                )
            )
        )

        viewModel = FeedViewModel(
            repository = repository,
            networkMonitor = networkMonitor
        )

        viewModel.toggleLike(post)

        val snapshot: List<Post> = viewModel.postsWithLikeState.asSnapshot()

        assertTrue(snapshot.first().isLikedByUser)
        assertEquals(6, snapshot.first().likeCount)

        coVerify {
            repository.toggleLike("1")
        }
    }
}
