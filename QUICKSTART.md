# Quick Start Guide - Habit Tracker

## Building the App

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or later
- Android SDK 24 or higher
- JDK 8 or higher

### Steps to Build

1. **Clone the Repository**
   ```bash
   git clone https://github.com/smartpad23/cabessa_project.git
   cd cabessa_project
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned repository folder
   - Click OK

3. **Sync Gradle**
   - Android Studio will automatically prompt to sync Gradle
   - Click "Sync Now" if prompted
   - Wait for dependencies to download

4. **Build the Project**
   ```bash
   ./gradlew build
   ```
   Or use Android Studio: Build → Make Project

5. **Run on Device/Emulator**
   - Connect an Android device or start an emulator
   - Click the "Run" button (green triangle) in Android Studio
   - Select your target device
   - Wait for the app to install and launch

## Using the App

### Adding Your First Habit

1. Tap the **+** (floating action button) at the bottom-right
2. Enter a habit name (e.g., "Morning Exercise")
3. Optionally add a description (e.g., "30 minutes of cardio")
4. Toggle "Enable Reminder" if you want notifications
5. Select a reminder time if enabled
6. Tap **SAVE**

### Completing a Habit

1. Find your habit in the list
2. Tap the **checkbox** on the right side
3. The streak counter will update
4. You can only complete a habit once per day

### Editing a Habit

1. Tap anywhere on the **habit card** (not the checkbox)
2. Modify the details
3. Tap **SAVE** to update

### Deleting a Habit

1. **Swipe left or right** on a habit card
2. Confirm deletion in the dialog
3. The habit and its reminder will be removed

## App Features at a Glance

| Feature | Description |
|---------|-------------|
| 📝 Create | Add new habits with custom names and descriptions |
| ✏️ Edit | Update habit details anytime |
| 🗑️ Delete | Swipe to remove habits |
| ✅ Complete | Check off daily completions |
| 🔥 Streaks | Track consecutive completion days |
| ⏰ Reminders | Set custom notification times |
| 📊 History | View last completion dates |

## Troubleshooting

### Build Errors

**Issue**: "Android SDK not found"
- **Solution**: Set ANDROID_HOME environment variable to your SDK location
  ```bash
  export ANDROID_HOME=/path/to/android/sdk
  ```

**Issue**: "Gradle sync failed"
- **Solution**: Check your internet connection and try:
  ```bash
  ./gradlew clean
  ./gradlew build
  ```

### Runtime Issues

**Issue**: "Notifications not appearing"
- **Solution**: Grant notification permission in device settings:
  Settings → Apps → Habit Tracker → Notifications → Enable

**Issue**: "Reminders not working"
- **Solution**: Check alarm permissions in device settings:
  Settings → Apps → Habit Tracker → Permissions → Alarms & reminders → Allow

## Minimum Requirements

- **Android Version**: 7.0 (Nougat) / API Level 24+
- **Storage**: 50 MB available space
- **RAM**: 1 GB minimum
- **Screen**: Any Android phone or tablet

## File Structure Reference

```
app/src/main/
├── java/com/example/habittracker/
│   ├── MainActivity.kt              # Main screen
│   ├── ReminderReceiver.kt          # Notification handler
│   ├── data/
│   │   ├── Habit.kt                 # Data model
│   │   ├── HabitDao.kt              # Database queries
│   │   ├── HabitDatabase.kt         # Database setup
│   │   └── HabitRepository.kt       # Data layer
│   ├── ui/
│   │   └── HabitAdapter.kt          # List adapter
│   └── viewmodel/
│       └── HabitViewModel.kt        # UI logic
└── res/
    ├── layout/                      # UI layouts
    ├── values/                      # Strings, colors, themes
    └── xml/                         # Configuration files
```

## Support

For issues or questions:
1. Check the IMPLEMENTATION.md for detailed technical documentation
2. Review the README.md for project overview
3. Open an issue on GitHub if problems persist

## Quick Tips

💡 **Best Practices:**
- Set realistic habits you can maintain daily
- Enable reminders for important habits
- Check off habits immediately after completion
- Review your streaks weekly for motivation

💡 **Performance:**
- The app stores all data locally
- No internet connection required
- Minimal battery usage
- Notifications use system alarms

💡 **Privacy:**
- All data stays on your device
- No analytics or tracking
- No account required
- Export feature coming soon

Happy habit tracking! 🎯
