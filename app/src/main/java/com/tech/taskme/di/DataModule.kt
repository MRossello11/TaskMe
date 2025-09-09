package com.tech.taskme.di

import android.content.Context
import androidx.room.Room
import com.tech.data.core.TaskMeDatabase
import com.tech.data.feature_tasks.entities.TaskEntityDao
import com.tech.data.feature_tasks.repository.TaskRepositoryImplementation
import com.tech.feature_tasks.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskMeDatabase {
        return Room.databaseBuilder(
            context,
            TaskMeDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: TaskMeDatabase): TaskEntityDao {
        return database.taskEntityDao()
    }

    @Provides
    fun provideTaskRepository(taskEntityDao: TaskEntityDao): TaskRepository {
        return TaskRepositoryImplementation(taskEntityDao)
    }
}
