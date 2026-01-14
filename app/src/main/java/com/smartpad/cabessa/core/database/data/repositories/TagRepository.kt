package com.smartpad.cabessa.core.database.data.repositories

import com.smartpad.cabessa.core.database.data.entities.TagEntity
import kotlinx.coroutines.flow.Flow

interface TagRepository {
    //Common tag operations
    fun getObserveActiveTags(): Flow<List<TagEntity>>
    suspend fun getByIdTag(id: Long): TagEntity?
    suspend fun insertTag(entity: TagEntity): Long
    suspend fun updateTag(entity: TagEntity)
    suspend fun softDeleteTag(id: Long)
    suspend fun deleteTag(entity: TagEntity)

    // Cross reference operations
    /**
     * Returns a reactive stream of tags for a specific drug, treatment, or dose.
     */
    fun getTagsByEntryStream(entryId: Long): Flow<List<TagEntity>>

    fun getEntriesByTagStream(tagId: Long): Flow<List<Long>>

    /**
     * Links a tag to a global entry.
     */
    suspend fun addTagToEntry(tagId: Long, entryId: Long)

    /**
     * Unlinks a tag from a global entry.
     */
    suspend fun removeTagFromEntry(tagId: Long, entryId: Long)
}