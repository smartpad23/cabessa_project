# Habit Tracker - Android App

An Android application for managing life habits and reminders.

## Features

- **Create and Manage Habits**: Add, edit, and delete daily habits
- **Track Progress**: Monitor your habit streaks and completion history
- **Daily Reminders**: Set custom reminder times for each habit
- **Mark as Complete**: Check off habits as you complete them each day
- **Streak Tracking**: Keep track of consecutive days you've maintained a habit
- **Simple UI**: Clean, Material Design interface for easy habit management

## Technical Stack

- **Language**: Kotlin
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room (SQLite)
- **UI**: Material Design Components
- **Async Operations**: Kotlin Coroutines
- **Notifications**: AlarmManager for scheduled reminders

## Requirements

- Android SDK 24 or higher
- Android Studio Arctic Fox or later
- Gradle 7.0+

## Building the Project

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

```bash
./gradlew build
```

## Permissions

The app requires the following permissions:
- `POST_NOTIFICATIONS` - To display habit reminder notifications
- `SCHEDULE_EXACT_ALARM` - To schedule exact alarm times for reminders
- `USE_EXACT_ALARM` - To use exact alarms on Android 14+

## Usage

1. **Add a Habit**: Tap the + button to create a new habit
2. **Set Reminders**: Enable reminders and choose a time for daily notifications
3. **Complete Habits**: Check the box next to a habit when you complete it
4. **Track Streaks**: View your current streak for each habit
5. **Edit or Delete**: Tap a habit to edit it, or swipe to delete

## Project Structure

```
app/
├── src/main/
│   ├── java/com/example/habittracker/
│   │   ├── data/           # Room database, entities, DAOs
│   │   ├── ui/             # RecyclerView adapters
│   │   ├── viewmodel/      # ViewModels for MVVM
│   │   ├── MainActivity.kt # Main UI controller
│   │   └── ReminderReceiver.kt # Notification handler
│   └── res/
│       ├── layout/         # XML layouts
│       ├── values/         # Strings, colors, themes
│       └── xml/            # Backup rules
└── build.gradle.kts        # App dependencies
```