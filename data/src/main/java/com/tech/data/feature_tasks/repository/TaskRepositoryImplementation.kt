package com.tech.data.feature_tasks.repository

import com.tech.data.feature_tasks.entities.TaskEntity
import com.tech.data.feature_tasks.entities.TaskEntityDao
import com.tech.data.feature_tasks.entities.toDomain
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository

class TaskRepositoryImplementation(
    private val taskEntityDao: TaskEntityDao
): TaskRepository {
    override suspend fun createTask(task: Task): Int {
        taskEntityDao.updateTask(task.toEntity())
        return 0
    }

    override suspend fun getTasks(): List<Task> {
        return taskEntityDao.getTasks().map {
            it.toDomain()
        }
    }

    override suspend fun deleteTask(id: String): Int {
        taskEntityDao.deleteTask(id)
        return 0
    }

    override suspend fun updateTask(task: Task): Int {
        taskEntityDao.updateTask(task.toEntity())
        return 0
    }
}

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    createdAt = createdAt,
    completed = completed,
    deleted = deleted
)