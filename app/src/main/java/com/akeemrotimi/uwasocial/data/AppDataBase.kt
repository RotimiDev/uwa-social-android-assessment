package com.akeemrotimi.uwasocial.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akeemrotimi.uwasocial.data.local.PostDao
import com.akeemrotimi.uwasocial.data.local.PostEntity

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
