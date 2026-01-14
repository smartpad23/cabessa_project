package com.smartpad.cabessa.modules.drugs.data.repositoryImpl

import com.smartpad.cabessa.core.database.data.dao.RegistryDao
import com.smartpad.cabessa.core.database.data.entities.EntityRegistry
import com.smartpad.cabessa.modules.drugs.data.dao.DrugDao
import com.smartpad.cabessa.modules.drugs.data.repositories.DrugRepository
import com.smartpad.cabessa.modules.drugs.data.entities.DrugEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementation of DrugRepository.
 *
 * Technical basis:
 * - When creating a new drug we first insert an EntityRegistry row to obtain a global id.
 * - We run DB write operations on Dispatchers.IO via withContext for safety.
 * - The repository isolates transaction logic from UI/ViewModel.
 */
class DrugRepositoryImpl(
    private val drugDao: DrugDao,
    private val registryDao: RegistryDao
) : DrugRepository {

    override fun observeActiveDrugs(): Flow<List<DrugEntity>> = drugDao.observeActive()

    override fun searchActiveDrugs(query: String): Flow<List<DrugEntity>> = drugDao.searchActive(query)

    override suspend fun getDrugById(id: Long): DrugEntity? = drugDao.getById(id)

    override suspend fun createDrug(drug: DrugEntity): Long {
        // Insert registry entry to obtain global id for the drug.
        val registryId = registryDao.insertRegistry(EntityRegistry())
        // Create a copy of the drug with the assigned id
        val drugWithId = drug.copy(id = registryId)
        drugDao.insert(drugWithId)
        return registryId
    }

    override suspend fun updateDrug(drug: DrugEntity) = drugDao.update(drug)

    override suspend fun softDeleteDrug(id: Long) = drugDao.softDelete(id)

    override suspend fun deleteDrug(drug: DrugEntity) = drugDao.delete(drug)
}