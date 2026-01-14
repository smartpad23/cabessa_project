package com.smartpad.cabessa.modules.drugs.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smartpad.cabessa.modules.drugs.data.entities.DrugEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO for DrugEntity.
 * Contains flows for reactive UI and suspend functions for one-shot queries.
 */
@Dao
interface DrugDao {
    /**
     * Observe all active drugs sorted by name ascending.
     * Used for displaying the main list reactively.
     */
    @Query("SELECT * FROM drugs WHERE isActive = 1 ORDER BY name ASC")
    fun observeActive(): Flow<List<DrugEntity>>

    /**
     * Get a single drug by id.
     */
    @Query("SELECT * FROM drugs WHERE id = :id")
    suspend fun getById(id: Long): DrugEntity?

    /**
     * Insert a drug. We use ABORT to avoid accidental overwrites; registry ensures unique id.
     * Returns rowId as Long.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(entity: DrugEntity): Long

    /**
     * Update a drug.
     */
    @Update
    suspend fun update(entity: DrugEntity)

    /**
     * Soft-delete a drug (isActive = 0) to preserve historical data.
     */
    @Query("UPDATE drugs SET isActive = 0 WHERE id = :id")
    suspend fun softDelete(id: Long)

    /**
     * Hard delete — only for exceptional administrative operations.
     */
    @Delete
    suspend fun delete(entity: DrugEntity)

    // --- Additional helpful queries for filters/search ---

    /**
     * Search by name or active substance (case-insensitive).
     */
    @Query("""
        SELECT * FROM drugs
        WHERE isActive = 1 AND (name LIKE '%' || :query || '%' OR activeSubstance LIKE '%' || :query || '%')
        ORDER BY name ASC
    """)
    fun searchActive(query: String): Flow<List<DrugEntity>>

    /**
     * Observe drugs ordered by stock ascending (for low-stock screens).
     */
    @Query("SELECT * FROM drugs WHERE isActive = 1 ORDER BY stockUnits ASC")
    fun observeByStockAsc(): Flow<List<DrugEntity>>
}