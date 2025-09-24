package com.tech.taskme.di

import android.content.Context
import androidx.room.Room
import com.tech.core.di.RetrofitTasks
import com.tech.core.di.RoomTasks
import com.tech.data.BuildConfig
import com.tech.data.core.TaskMeDatabase
import com.tech.data.feature_tasks.data_source.TaskRetrofitDataSource
import com.tech.data.feature_tasks.entities.TaskEntityDao
import com.tech.data.feature_tasks.repository.TaskRepositoryImplementation
import com.tech.data.feature_tasks.repository.TaskRetrofitRepositoryImplementation
import com.tech.feature_tasks.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideTaskApiService(): TaskRetrofitDataSource  {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
            .create(TaskRetrofitDataSource::class.java)
    }

    @Provides
    @RoomTasks
    fun provideTaskRepositoryImplementation(taskEntityDao: TaskEntityDao): TaskRepository {
        return TaskRepositoryImplementation(taskEntityDao)
    }

    @Provides
    @RetrofitTasks
    fun provideTaskRetrofitRepositoryImplementation(taskRetrofitDataSource: TaskRetrofitDataSource): TaskRepository {
        return TaskRetrofitRepositoryImplementation(taskRetrofitDataSource)
    }
}
