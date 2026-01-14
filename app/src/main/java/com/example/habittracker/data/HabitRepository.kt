package com.example.habittracker.data

import kotlinx.coroutines.flow.Flow

class HabitRepository(private val habitDao: HabitDao) {
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()
    
    suspend fun insert(habit: Habit): Long {
        return habitDao.insert(habit)
    }
    
    suspend fun update(habit: Habit) {
        habitDao.update(habit)
    }
    
    suspend fun delete(habit: Habit) {
        habitDao.delete(habit)
    }
    
    suspend fun getHabitById(id: Long): Habit? {
        return habitDao.getHabitById(id)
    }
}
