package com.tech.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tech.data.feature_sync_changes.entities.ChangeEntity
import com.tech.data.feature_sync_changes.entities.ChangeEntityDao
import com.tech.data.feature_sync_changes.entities.SyncEntity
import com.tech.data.feature_sync_changes.entities.SyncEntityDao
import com.tech.data.feature_tasks.entities.TaskEntity
import com.tech.data.feature_tasks.entities.TaskEntityDao

@Database(
   entities = [
       TaskEntity::class,
       ChangeEntity::class,
       SyncEntity::class
   ],
    version = 2
)
abstract class TaskMeDatabase: RoomDatabase() {
    abstract fun taskEntityDao(): TaskEntityDao
    abstract fun changeEntityDao(): ChangeEntityDao
    abstract fun syncEntityDao(): SyncEntityDao
}