package com.tech.feature_tasks.domain.repository

import com.tech.feature_tasks.domain.model.Task

interface TaskRepository {
    suspend fun createTask(task: Task): Int
    suspend fun getTasks(): List<Task>
    suspend fun deleteTask(id: String): Int
    suspend fun updateTask(task: Task): Int
}