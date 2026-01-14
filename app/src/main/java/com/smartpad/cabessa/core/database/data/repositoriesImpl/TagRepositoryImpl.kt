package com.smartpad.cabessa.core.database.data.repositoriesImpl

import com.smartpad.cabessa.core.database.AppDatabase
import com.smartpad.cabessa.core.database.data.dao.TagDao
import com.smartpad.cabessa.core.database.data.entities.TagEntity
import com.smartpad.cabessa.core.database.data.entities.TagEntryCrossRef
import com.smartpad.cabessa.core.database.data.repositories.TagRepository
import kotlinx.coroutines.flow.Flow

class TagRepositoryImpl (
    private val tagDao: TagDao,
    private val database: AppDatabase
) : TagRepository {
    override fun getObserveActiveTags(): Flow<List<TagEntity>> = tagDao.observeActive()

    override suspend fun getByIdTag(id: Long): TagEntity? =
        tagDao.getById(id)

    override suspend fun insertTag(entity: TagEntity): Long =
        tagDao.insert(entity)

    override suspend fun updateTag(entity: TagEntity) =
        tagDao.update(entity)

    override suspend fun softDeleteTag(id: Long) =
        tagDao.softDelete(id)

    override suspend fun deleteTag(entity: TagEntity) =
        tagDao.delete(entity)

    override fun getTagsByEntryStream(entryId: Long): Flow<List<TagEntity>> =
        tagDao.getTagsForEntry(entryId)

    override fun getEntriesByTagStream(tagId: Long): Flow<List<Long>> =
        tagDao.getEntriesForTag(tagId)

    /**
     * Technical Basis: We use 'suspend' for one-shot write operations.
     * This ensures the call is non-blocking for the UI thread.
     */

    override suspend fun addTagToEntry(tagId: Long, entryId: Long) {
        val crossRef = TagEntryCrossRef(tagId = tagId, entryId = entryId)
        tagDao.insertCrossRef(crossRef)
    }

    override suspend fun removeTagFromEntry(tagId: Long, entryId: Long) =
        tagDao.deleteCrossRef(tagId, entryId)

}