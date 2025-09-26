package com.tech.data.feature_tasks.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.PrimaryKey
import androidx.room.Query
import com.tech.feature_tasks.domain.model.Task

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val title: String,
    val createdAt: Long = System.currentTimeMillis(),
    val completed: Boolean = false,
    val deleted: Boolean = false
)

fun TaskEntity.toDomain() = Task(
    id = id,
    title = title,
    createdAt = createdAt,
    completed = completed
)

@Dao
interface TaskEntityDao {
    @Query("SELECT * FROM TaskEntity")
    suspend fun getTasks(): List<TaskEntity>

    @Insert(onConflict = REPLACE)
    suspend fun updateTask(task: TaskEntity)

    @Query("DELETE FROM TaskEntity WHERE id = :id")
    suspend fun deleteTask(id: String)
}