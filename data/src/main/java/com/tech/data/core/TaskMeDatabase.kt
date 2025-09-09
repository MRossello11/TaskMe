package com.tech.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tech.data.feature_tasks.entities.TaskEntity
import com.tech.data.feature_tasks.entities.TaskEntityDao

@Database(
   entities = [
       TaskEntity::class,
   ],
    version = 1
)
abstract class TaskMeDatabase: RoomDatabase() {
    abstract fun taskEntityDao(): TaskEntityDao
}