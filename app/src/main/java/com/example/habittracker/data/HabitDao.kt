package com.example.habittracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY createdAt DESC")
    fun getAllHabits(): Flow<List<Habit>>
    
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: Long): Habit?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit): Long
    
    @Update
    suspend fun update(habit: Habit)
    
    @Delete
    suspend fun delete(habit: Habit)
    
    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteById(id: Long)
}
