package com.akeemrotimi.uwasocial.domain.model

data class Post(
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
