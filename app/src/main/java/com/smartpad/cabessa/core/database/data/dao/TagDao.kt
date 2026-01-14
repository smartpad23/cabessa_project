package com.smartpad.cabessa.core.database.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smartpad.cabessa.core.database.data.entities.TagEntity
import com.smartpad.cabessa.core.database.data.entities.TagEntryCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao {
    // --- Operations for Tags ---
    @Query("SELECT * FROM tags WHERE isActive = 1 ORDER BY name ASC")
    fun observeActive(): Flow<List<TagEntity>>

    @Query("SELECT * FROM tags WHERE tagId = :id")
    suspend fun getById(id: Long): TagEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: TagEntity): Long

    @Update
    suspend fun update(entity: TagEntity)

    @Query("UPDATE tags SET isActive = 0 WHERE tagId = :id")
    suspend fun softDelete(id: Long)

    @Delete
    suspend fun delete(entity: TagEntity)


    // --- Operations for Relationships (CrossRef) ---

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRef(crossRef: TagEntryCrossRef)

    @Query("DELETE FROM tag_entry_cross_ref WHERE tagId = :tagId AND entryId = :entryId")
    suspend fun deleteCrossRef(tagId: Long, entryId: Long)

    /**
     * Technical Basis: This query performs a JOIN between the cross-ref table
     * and the tags table to get all tags for a specific global entity.
     */
    @Query("""
        SELECT tags.* FROM tags 
        INNER JOIN tag_entry_cross_ref ON tags.tagId = tag_entry_cross_ref.tagId 
        WHERE tag_entry_cross_ref.entryId = :entryId
    """)
    fun getTagsForEntry(entryId: Long): Flow<List<TagEntity>>

    /**
     * Reusable query to find all global IDs associated with a specific tag.
     * Useful for global search/filtering across modules.
     */
    @Query("SELECT entryId FROM tag_entry_cross_ref WHERE tagId = :tagId")
    fun getEntriesForTag(tagId: Long): Flow<List<Long>>
}