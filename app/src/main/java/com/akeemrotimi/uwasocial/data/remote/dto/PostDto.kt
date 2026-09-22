package com.akeemrotimi.uwasocial.data.remote.dto

data class PostDto(
    val id: String,
    val userName: String,
    val userAvatarUrl: String,
    val text: String,
    val mediaUrl: String?,
    val location: String?,
    val timestampMillis: Long,
    val likeCount: Int,
    val commentCount: Int,
    val isLikedByUser: Boolean
)

data class PostsPageDto(
    val posts: List<PostDto>,
    val page: Int,
    val isLastPage: Boolean
)
