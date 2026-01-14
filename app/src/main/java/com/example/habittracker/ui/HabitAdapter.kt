package com.example.habittracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.data.Habit
import java.text.SimpleDateFormat
import java.util.*

class HabitAdapter(
    private val onHabitClick: (Habit) -> Unit,
    private val onHabitComplete: (Habit) -> Unit
) : ListAdapter<Habit, HabitAdapter.HabitViewHolder>(HabitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_habit, parent, false)
        return HabitViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val habitName: TextView = itemView.findViewById(R.id.habitName)
        private val habitDescription: TextView = itemView.findViewById(R.id.habitDescription)
        private val habitStreak: TextView = itemView.findViewById(R.id.habitStreak)
        private val habitLastCompleted: TextView = itemView.findViewById(R.id.habitLastCompleted)
        private val checkComplete: CheckBox = itemView.findViewById(R.id.checkComplete)

        fun bind(habit: Habit) {
            habitName.text = habit.name
            habitDescription.text = habit.description
            habitStreak.text = itemView.context.getString(R.string.streak, habit.streak)
            
            val lastCompletedText = if (habit.lastCompletedDate != null) {
                val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                itemView.context.getString(
                    R.string.last_completed,
                    dateFormat.format(Date(habit.lastCompletedDate))
                )
            } else {
                itemView.context.getString(R.string.last_completed, itemView.context.getString(R.string.never))
            }
            habitLastCompleted.text = lastCompletedText
            
            // Check if completed today
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
            
            val completedToday = habit.lastCompletedDate?.let { lastCompleted ->
                val lastCompletedDay = Calendar.getInstance().apply {
                    timeInMillis = lastCompleted
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                lastCompletedDay == today
            } ?: false
            
            checkComplete.isChecked = completedToday
            
            itemView.setOnClickListener {
                onHabitClick(habit)
            }
            
            checkComplete.setOnClickListener {
                if (!completedToday) {
                    onHabitComplete(habit)
                }
            }
        }
    }

    class HabitDiffCallback : DiffUtil.ItemCallback<Habit>() {
        override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
            return oldItem == newItem
        }
    }
}
