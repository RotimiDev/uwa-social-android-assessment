package com.akeemrotimi.uwasocial.data.mapper

import com.akeemrotimi.uwasocial.data.local.PostEntity
import com.akeemrotimi.uwasocial.data.remote.dto.PostDto
import com.akeemrotimi.uwasocial.domain.model.Post

fun PostDto.toEntity(page: Int): PostEntity = PostEntity(
    id = id,
    page = page,
    userName = userName,
    userAvatarUrl = userAvatarUrl,
    text = text,
    mediaUrl = mediaUrl,
    location = location,
    timestampMillis = timestampMillis,
    likeCount = likeCount,
    commentCount = commentCount,
    isLikedByUser = isLikedByUser
)

fun PostEntity.toDomain(): Post = Post(
    id = id,
    userName = userName,
    userAvatarUrl = userAvatarUrl,
    text = text,
    mediaUrl = mediaUrl,
    location = location,
    timestampMillis = timestampMillis,
    likeCount = likeCount,
    commentCount = commentCount,
    isLikedByUser = isLikedByUser
)

fun PostDto.toDomain(): Post = Post(
    id = id,
    userName = userName,
    userAvatarUrl = userAvatarUrl,
    text = text,
    mediaUrl = mediaUrl,
    location = location,
    timestampMillis = timestampMillis,
    likeCount = likeCount,
    commentCount = commentCount,
    isLikedByUser = isLikedByUser
)
