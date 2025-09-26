package com.tech.data.feature_tasks.repository

import com.tech.data.feature_tasks.data_source.TaskRetrofitDataSource
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository

class TaskRetrofitRepositoryImplementation(
    private val apiService: TaskRetrofitDataSource
): TaskRepository {
    override suspend fun createTask(task: Task): Int {
        return apiService.createTask(task)
    }

    override suspend fun getTasks(): List<Task> {
        return apiService.getAllTasks()
    }

    override suspend fun deleteTask(id: String): Int {
        return apiService.deleteTask(id)
    }

    override suspend fun updateTask(task: Task): Int {
        return apiService.updateTask(task)
    }
}