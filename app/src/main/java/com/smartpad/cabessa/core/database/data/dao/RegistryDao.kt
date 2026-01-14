package com.smartpad.cabessa.core.database.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.smartpad.cabessa.core.database.data.entities.EntityRegistry

@Dao
interface RegistryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRegistry(registry: EntityRegistry): Long
}