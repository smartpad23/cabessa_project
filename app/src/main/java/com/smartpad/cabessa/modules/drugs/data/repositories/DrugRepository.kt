package com.smartpad.cabessa.modules.drugs.data.repositories

import com.smartpad.cabessa.modules.drugs.data.entities.DrugEntity
import kotlinx.coroutines.flow.Flow

/**
 * Repository abstraction for Drugs.
 * The repository handles coordination with EntityRegistry for id generation and with DAOs.
 */
interface DrugRepository {
    fun observeActiveDrugs(): Flow<List<DrugEntity>>
    fun searchActiveDrugs(query: String): Flow<List<DrugEntity>>
    suspend fun getDrugById(id: Long): DrugEntity?
    suspend fun createDrug(drug: DrugEntity): Long
    suspend fun updateDrug(drug: DrugEntity)
    suspend fun softDeleteDrug(id: Long)
    suspend fun deleteDrug(drug: DrugEntity) // Exceptional
}