package com.tech.data.feature_sync_changes.entities

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.PrimaryKey
import androidx.room.Query
import com.tech.core.enums.ChangeType
import com.tech.feature_sync_changes.domain.model.Change

@Entity
class ChangeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val type: ChangeType,
    val objectId: String,
    val timesTamp: Long
)

fun ChangeEntity.toDomain() = Change(
    id = id,
    type = type,
    objectId = objectId,
    timesTamp = timesTamp
)

@Dao
interface ChangeEntityDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertChange(changeEntity: ChangeEntity): Long

    @Query("SELECT * FROM ChangeEntity")
    suspend fun getChanges(): List<ChangeEntity>

    @Query("DELETE FROM ChangeEntity WHERE id = :id")
    suspend fun deleteChange(id: Int)
}