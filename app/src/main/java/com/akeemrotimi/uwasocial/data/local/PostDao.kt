package com.akeemrotimi.uwasocial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PostDao {

    @Query("SELECT * FROM posts WHERE page = :page ORDER BY timestampMillis DESC")
    suspend fun getPage(page: Int): List<PostEntity>

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun countAll(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Query("UPDATE posts SET isLikedByUser = :isLiked, likeCount = :likeCount WHERE id = :postId")
    suspend fun updateLike(postId: String, isLiked: Boolean, likeCount: Int)

    @Query("SELECT * FROM posts WHERE id = :postId LIMIT 1")
    suspend fun getById(postId: String): PostEntity?

    @Query("DELETE FROM posts WHERE page = :page")
    suspend fun clearPage(page: Int)

    @Transaction
    suspend fun replacePage(page: Int, posts: List<PostEntity>) {
        clearPage(page)
        insertAll(posts)
    }
}
