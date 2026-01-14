package com.smartpad.cabessa.core.database.migrations

import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.migration.AutoMigrationSpec

//EXAMPLE MIGRATION SPECS
/**
 * Room will automatically handle simple schema changes like adding new entities,
 * adding new columns, or changing column types.
 * However, for more complex changes like renaming or deleting columns/tables, we need to provide
 * explicit instructions via AutoMigrationSpec classes.
 */

/**
 * Case 1: Renaming a column.
 * We tell Room that 'brand' is now called 'manufacturer' in 'drugs' table.
 */
@RenameColumn(tableName = "drugs", fromColumnName = "brand", toColumnName = "manufacturer")
class RenameBrandSpec : AutoMigrationSpec

/**
 * Case 2: Deleting a column.
 * We confirm that we want to permanently remove 'comments' from 'treatments'.
 */
@DeleteColumn(tableName = "treatments", columnName = "comments")
class DeleteCommentsSpec : AutoMigrationSpec

/**
 * Case 3: Renaming a table.
 * If we decided to change 'dosis' to 'doses' (English plural correction).
 */
@RenameTable(fromTableName = "dosis", toTableName = "doses")
class RenameDoseTableSpec : AutoMigrationSpec

/**
 * Case 4: Accumulative changes.
 * If there are many changes in one migration, we can combine them.
 */
@RenameColumn(tableName = "drugs", fromColumnName = "old_name", toColumnName = "name")
@RenameColumn(tableName = "drugs", fromColumnName = "old_brand", toColumnName = "brand")
@DeleteColumn(tableName = "treatments", columnName = "legacy_data")
@RenameTable(fromTableName = "dosis", toTableName = "doses")
class MigrationSpecV2ToV3 : AutoMigrationSpec

//////////////////////////////////////////////////////////////////////////////////////////

// REAL MIGRATION SPECS FOR THE PROJECT