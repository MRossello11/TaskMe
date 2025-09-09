package com.tech.data.feature_tasks.repository

import com.tech.data.feature_tasks.entities.TaskEntity
import com.tech.data.feature_tasks.entities.TaskEntityDao
import com.tech.data.feature_tasks.entities.toDomain
import com.tech.feature_tasks.domain.model.Task
import com.tech.feature_tasks.domain.repository.TaskRepository

class TaskRepositoryImplementation(
    private val taskEntityDao: TaskEntityDao
): TaskRepository {
    override suspend fun createTask(task: Task): Task {
        val id = taskEntityDao.updateTask(task.toEntity())
        return task.copy(id = id)
    }

    override suspend fun getTasks(): List<Task> {
        return taskEntityDao.getTasks().map {
            it.toDomain()
        }
    }

    override suspend fun deleteTask(id: Long) {
        taskEntityDao.deleteTask(id)
    }

    override suspend fun updateTask(task: Task) {
        taskEntityDao.updateTask(task.toEntity())
    }
}

fun Task.toEntity() = TaskEntity(
    id = id,
    title = title,
    createdAt = createdAt,
    completed = completed
)