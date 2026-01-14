package com.smartpad.cabessa.modules.drugs.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smartpad.cabessa.modules.drugs.common.enums.EnumUnitTypes

/**
 * DrugEntity represents a medication entry.
 *
 * Technical design choices:
 * - We avoid inheriting Room-mapped properties from a superclass to reduce mapping surprises.
 * - The entity contains explicit fields for `id`, `creationDate`, and `isActive`.
 * - Primary key `id` is assigned from EntityRegistry (global id) to allow cross-module references.
 */
@Entity(tableName = "drugs")
data class DrugEntity(
    @PrimaryKey
    val id: Long, // Assigned from EntityRegistry (global id)
    val creationDate: Long = System.currentTimeMillis(),
    val name: String,
    val brand: String,
    val quantityPerBox: Double,
    val activeSubstance: String,
    val stockUnits: Double = 0.0,
    val unitType: EnumUnitTypes,
    val comments: String? = null,
    val isActive: Boolean = true,
    val userId: Long? = null // Prepared for future multi-user feature
)
