package com.akeemrotimi.uwasocial.data.remote

import com.akeemrotimi.uwasocial.data.remote.dto.PostDto
import com.akeemrotimi.uwasocial.data.remote.dto.PostsPageDto
import kotlinx.coroutines.delay
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

interface PostApiService {
    suspend fun getPosts(page: Int, pageSize: Int): PostsPageDto
}

@Singleton
class MockPostApiService @Inject constructor() : PostApiService {

    companion object {
        private const val TOTAL_POSTS = 47
        private const val FAILURE_RATE = 0.12
        private const val NETWORK_DELAY_MS = 900L

        private val NAMES = listOf(
            "Chidera Obi", "Amara Nwosu", "Tunde Bakare", "Ify Eze", "Bola Adeyemi",
            "Ngozi Umeh", "Femi Adekunle", "Zainab Musa", "Emeka Chukwu", "Halima Bello"
        )
        private val LOCATIONS = listOf(
            "Port Harcourt, NG", "Lagos, NG", "Abuja, NG", "Enugu, NG", "Ibadan, NG", null
        )
        private val SNIPPETS = listOf(
            "Shipped a new feature today, feeling good about the release.",
            "Sunday service was refreshing. Grateful for community.",
            "Debugging paging edge cases for way too long today \uD83D\uDE05",
            "Coffee, code, repeat.",
            "Anyone else's build suddenly breaking on CI?",
            "Weekend hike recap incoming.",
            "Small wins count too."
        )
    }

    override suspend fun getPosts(page: Int, pageSize: Int): PostsPageDto {
        delay(NETWORK_DELAY_MS)

        if (Random.nextDouble() < FAILURE_RATE) {
            throw IOException("Simulated network failure while loading page $page")
        }

        val startIndex = (page - 1) * pageSize
        if (startIndex >= TOTAL_POSTS) {
            return PostsPageDto(posts = emptyList(), page = page, isLastPage = true)
        }

        val endIndexExclusive = minOf(startIndex + pageSize, TOTAL_POSTS)
        val posts = (startIndex until endIndexExclusive).map { index ->
            buildPost(index)
        }

        return PostsPageDto(
            posts = posts,
            page = page,
            isLastPage = endIndexExclusive >= TOTAL_POSTS
        )
    }

    private fun buildPost(index: Int): PostDto {
        val name = NAMES[index % NAMES.size]
        val hasMedia = index % 3 != 0
        return PostDto(
            id = "post_$index",
            userName = name,
            userAvatarUrl = "https://picsum.photos/seed/avatar$index/96/96",
            text = SNIPPETS[index % SNIPPETS.size],
            mediaUrl = if (hasMedia) "https://picsum.photos/seed/post$index/800/500" else null,
            location = LOCATIONS[index % LOCATIONS.size],
            timestampMillis = System.currentTimeMillis() - (index * 45 * 60 * 1000L),
            likeCount = (index * 7) % 340,
            commentCount = (index * 3) % 58,
            isLikedByUser = index % 5 == 0
        )
    }
}
