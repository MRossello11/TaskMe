package com.tech.data.feature_tasks.repository

import com.tech.data.feature_tasks.data_source.TaskRetrofitDataSource
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository

class TaskRetrofitRepositoryImplementation(
    private val apiService: TaskRetrofitDataSource
): TaskRepository {
    override suspend fun createTask(task: Task): Int {
        return try {
            apiService.createTask(task)
        } catch (_: Exception) {
            -1
        }
    }

    override suspend fun getTasks(): List<Task> {
        return try {
            apiService.getAllTasks()
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun deleteTask(id: String): Int {
        return try {
            apiService.deleteTask(id)
        } catch (_: Exception) {
            -1
        }
    }

    override suspend fun updateTask(task: Task): Int {
        return try{
            apiService.updateTask(task)
        } catch (_: Exception) {
            -1
        }
    }
}