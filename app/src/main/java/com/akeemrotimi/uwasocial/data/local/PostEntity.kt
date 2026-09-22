package com.akeemrotimi.uwasocial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val page: Int,
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
