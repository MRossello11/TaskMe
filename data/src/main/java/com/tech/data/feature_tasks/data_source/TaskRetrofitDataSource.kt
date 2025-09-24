package com.tech.data.feature_tasks.data_source

import com.tech.feature_tasks.domain.model.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TaskRetrofitDataSource {

    @GET("/tasks")
    suspend fun getAllTasks(): List<Task>

    @POST("/tasks")
    suspend fun createTask(@Body task: Task): Int

    @PUT("/tasks")
    suspend fun updateTask(@Body task: Task): Int

    @DELETE("/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Int
}