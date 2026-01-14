package com.smartpad.cabessa.core.database.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tags", indices = [Index(value = ["name"], unique = true)])
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Long = 0,
    val name: String,
    val color: String, // M3 Color Hex
    val isActive: Boolean
)