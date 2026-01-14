package com.smartpad.cabessa.core.database

import androidx.room.Database

import com.smartpad.cabessa.core.database.data.entities.EntityRegistry
import com.smartpad.cabessa.core.database.data.entities.TagEntity
import com.smartpad.cabessa.core.database.data.entities.TagEntryCrossRef
import com.smartpad.cabessa.core.database.data.dao.RegistryDao
import com.smartpad.cabessa.core.database.data.dao.TagDao
import com.smartpad.cabessa.core.database.converters.DateConverters
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.smartpad.cabessa.core.database.converters.UnitTypeConverters
import com.smartpad.cabessa.modules.drugs.data.dao.DrugDao
import com.smartpad.cabessa.modules.drugs.data.entities.DrugEntity

@Database(
    entities = [
        EntityRegistry::class,
        DrugEntity::class,
        TagEntity::class,
        TagEntryCrossRef::class,
    ],
    version = 1,
    autoMigrations = [
//        VERSION 1 -> 2: Automatic (e.g., added a new table or a new nullable column)
//        AutoMigration(from = 1, to = 2),
//
//        VERSION 2 -> 3: Manual Rename (Requires Spec)
//        AutoMigration(from = 2, to = 3, spec = MigrationSpecV2ToV3::class),
//
    ],
    exportSchema = true
)
@TypeConverters(DateConverters::class, UnitTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    // --- Registry & Core DAOs ---
    abstract fun registryDao(): RegistryDao
    abstract fun tagDao(): TagDao

    // --- Drugs module DAOs ---
    abstract fun drugDao(): DrugDao

    companion object {
        private const val DATABASE_NAME = "cabessa_db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Singleton pattern for the Database instance.
         * Technical Basis: @Volatile ensures visibility across threads.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // En un entorno profesional, aquí configuraríamos la migración automática
                    .fallbackToDestructiveMigration() // OJO: Borra datos si cambias versión sin migración
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}