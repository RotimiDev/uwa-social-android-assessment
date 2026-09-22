package com.akeemrotimi.uwasocial.di

import android.content.Context
import androidx.room.Room
import com.akeemrotimi.uwasocial.data.AppDatabase
import com.akeemrotimi.uwasocial.data.local.PostDao
import com.akeemrotimi.uwasocial.data.remote.MockPostApiService
import com.akeemrotimi.uwasocial.data.remote.PostApiService
import com.akeemrotimi.uwasocial.domain.repository.PostRepository
import com.akeemrotimi.uwasocial.domain.repository.PostRepositoryImpl
import com.akeemrotimi.uwasocial.domain.util.NetworkMonitor
import com.akeemrotimi.uwasocial.domain.util.NetworkMonitorImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "social_feed.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providePostDao(database: AppDatabase): PostDao = database.postDao()

    @Provides
    @Singleton
    fun providePostApiService(): PostApiService = MockPostApiService()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    @Singleton
    abstract fun bindPostRepository(impl: PostRepositoryImpl): PostRepository

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(impl: NetworkMonitorImpl): NetworkMonitor
}
