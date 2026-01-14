package com.example.habittracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.habittracker.data.Habit
import com.example.habittracker.data.HabitDatabase
import com.example.habittracker.data.HabitRepository
import kotlinx.coroutines.launch
import java.util.Calendar

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: HabitRepository
    val allHabits: LiveData<List<Habit>>
    
    init {
        val habitDao = HabitDatabase.getDatabase(application).habitDao()
        repository = HabitRepository(habitDao)
        allHabits = repository.allHabits.asLiveData()
    }
    
    suspend fun insert(habit: Habit): Long {
        return repository.insert(habit)
    }
    
    fun update(habit: Habit) = viewModelScope.launch {
        repository.update(habit)
    }
    
    fun delete(habit: Habit) = viewModelScope.launch {
        repository.delete(habit)
    }
    
    fun markHabitComplete(habit: Habit) = viewModelScope.launch {
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        
        val lastCompleted = habit.lastCompletedDate
        val newStreak = if (lastCompleted != null) {
            val yesterday = today - 24 * 60 * 60 * 1000
            val lastCompletedDay = Calendar.getInstance().apply {
                timeInMillis = lastCompleted
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            when {
                lastCompletedDay == today -> habit.streak // Already completed today
                lastCompletedDay == yesterday -> habit.streak + 1 // Consecutive day
                else -> 1 // Streak broken, start over
            }
        } else {
            1 // First completion
        }
        
        val updatedHabit = habit.copy(
            streak = newStreak,
            lastCompletedDate = System.currentTimeMillis()
        )
        repository.update(updatedHabit)
    }
}
