package com.smartpad.cabessa.core.database.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "tag_entry_cross_ref",
    primaryKeys = ["tagId", "entryId"],
    indices = [
        Index(value = ["entryId"]),
        Index(value = ["tagId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = TagEntity::class,
            parentColumns = ["tagId"],
            childColumns = ["tagId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EntityRegistry::class,
            parentColumns = ["globalId"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class TagEntryCrossRef(
    val tagId: Long,
    val entryId: Long // This is the globalId from any module
)