package com.example.habittracker

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.data.Habit
import com.example.habittracker.databinding.ActivityMainBinding
import com.example.habittracker.databinding.DialogAddHabitBinding
import com.example.habittracker.ui.HabitAdapter
import com.example.habittracker.viewmodel.HabitViewModel
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HabitViewModel
    private lateinit var adapter: HabitAdapter
    
    companion object {
        private const val CHANNEL_ID = "habit_reminders"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        createNotificationChannel()
        
        viewModel = ViewModelProvider(this)[HabitViewModel::class.java]
        
        setupRecyclerView()
        observeHabits()
        
        binding.fabAdd.setOnClickListener {
            showAddHabitDialog()
        }
    }
    
    private fun setupRecyclerView() {
        adapter = HabitAdapter(
            onHabitClick = { habit ->
                showEditHabitDialog(habit)
            },
            onHabitComplete = { habit ->
                viewModel.markHabitComplete(habit)
                Toast.makeText(this, R.string.completed, Toast.LENGTH_SHORT).show()
            }
        )
        
        binding.recyclerView.adapter = adapter
        
        // Swipe to delete
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val habit = adapter.currentList[position]
                
                AlertDialog.Builder(this@MainActivity)
                    .setTitle(R.string.delete)
                    .setMessage("Delete ${habit.name}?")
                    .setPositiveButton(R.string.delete) { _, _ ->
                        viewModel.delete(habit)
                        cancelReminder(habit)
                        Toast.makeText(this@MainActivity, R.string.habit_deleted, Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->
                        adapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        adapter.notifyItemChanged(position)
                    }
                    .show()
            }
        })
        
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }
    
    private fun observeHabits() {
        viewModel.allHabits.observe(this) { habits ->
            habits?.let { 
                adapter.submitList(it)
                if (it.isEmpty()) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }
    
    private fun showAddHabitDialog() {
        val dialogBinding = DialogAddHabitBinding.inflate(LayoutInflater.from(this))
        var selectedHour = 9
        var selectedMinute = 0
        
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.add_habit)
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            dialogBinding.reminderTimeLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        
        dialogBinding.btnSelectTime.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                dialogBinding.btnSelectTime.text = String.format("%02d:%02d", hour, minute)
            }, selectedHour, selectedMinute, true).show()
        }
        
        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.editHabitName.text.toString().trim()
            val description = dialogBinding.editHabitDescription.text.toString().trim()
            val reminderEnabled = dialogBinding.switchReminder.isChecked
            
            if (name.isNotEmpty()) {
                val habit = Habit(
                    name = name,
                    description = description,
                    reminderEnabled = reminderEnabled,
                    reminderHour = selectedHour,
                    reminderMinute = selectedMinute
                )
                viewModel.insert(habit)
                
                if (reminderEnabled) {
                    scheduleReminder(habit)
                }
                
                Toast.makeText(this, R.string.habit_added, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a habit name", Toast.LENGTH_SHORT).show()
            }
        }
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    private fun showEditHabitDialog(habit: Habit) {
        val dialogBinding = DialogAddHabitBinding.inflate(LayoutInflater.from(this))
        var selectedHour = habit.reminderHour
        var selectedMinute = habit.reminderMinute
        
        dialogBinding.editHabitName.setText(habit.name)
        dialogBinding.editHabitDescription.setText(habit.description)
        dialogBinding.switchReminder.isChecked = habit.reminderEnabled
        dialogBinding.btnSelectTime.text = String.format("%02d:%02d", habit.reminderHour, habit.reminderMinute)
        
        if (habit.reminderEnabled) {
            dialogBinding.reminderTimeLayout.visibility = View.VISIBLE
        }
        
        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.edit)
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            dialogBinding.reminderTimeLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        
        dialogBinding.btnSelectTime.setOnClickListener {
            TimePickerDialog(this, { _, hour, minute ->
                selectedHour = hour
                selectedMinute = minute
                dialogBinding.btnSelectTime.text = String.format("%02d:%02d", hour, minute)
            }, selectedHour, selectedMinute, true).show()
        }
        
        dialogBinding.btnSave.setOnClickListener {
            val name = dialogBinding.editHabitName.text.toString().trim()
            val description = dialogBinding.editHabitDescription.text.toString().trim()
            val reminderEnabled = dialogBinding.switchReminder.isChecked
            
            if (name.isNotEmpty()) {
                val updatedHabit = habit.copy(
                    name = name,
                    description = description,
                    reminderEnabled = reminderEnabled,
                    reminderHour = selectedHour,
                    reminderMinute = selectedMinute
                )
                viewModel.update(updatedHabit)
                
                if (reminderEnabled) {
                    scheduleReminder(updatedHabit)
                } else {
                    cancelReminder(updatedHabit)
                }
                
                Toast.makeText(this, R.string.habit_updated, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please enter a habit name", Toast.LENGTH_SHORT).show()
            }
        }
        
        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.show()
    }
    
    private fun scheduleReminder(habit: Habit) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("habit_id", habit.id)
            putExtra("habit_name", habit.name)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            habit.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, habit.reminderHour)
            set(Calendar.MINUTE, habit.reminderMinute)
            set(Calendar.SECOND, 0)
            
            // If the time has already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
    
    private fun cancelReminder(habit: Habit) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            habit.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        alarmManager.cancel(pendingIntent)
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Habit Reminders"
            val descriptionText = "Notifications for habit reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
