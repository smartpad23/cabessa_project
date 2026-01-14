package com.smartpad.cabessa.core.database.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entity_registry")
data class EntityRegistry(
    @PrimaryKey(autoGenerate = true) val globalId: Long = 0
)