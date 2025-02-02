# Setup Guide to Build and Run the App

## [App Link](https://drive.google.com/file/d/1PQhPgHj4cUvQAphH-2jYNZJJy0htj4da/view?usp=sharing)

## Prerequisites
- Android Studio (latest version)
- Git installed on your system
- JDK 17 or higher
- Minimum 8GB RAM recommended
- USB debugging enabled on your Android device (for physical testing)

## Installation Steps

### 1. Clone the Repository
```bash
git clone https://github.com/ankeshbanerjee/swipe-android-assignment.git
cd swipe-android-assignment
```

### 2. Open Project in Android Studio

- Launch Android Studio
- Select "Open an Existing Project"
- Navigate to the cloned repository folder
- Click "OK" to open

### 3. Project Setup

- Wait for Gradle sync to complete
- Install any required SDK packages if prompted
- Accept any license agreements if requested

### 4. Build the Project

- Click "Build → Make Project" or press Ctrl+F9 (Windows/Linux) or Cmd+F9 (macOS)
- Wait for the build process to complete

### 5. Run the App

- Connect your Android device or start an emulator
- Click "Run → Run 'app'" or press Shift+F10 (Windows/Linux) or Ctrl+R (macOS)
- Select your target device and click "OK"

## Technical Stack
- MVVM architecture 
- Retrofit for REST 
- KOIN for Dependency Injection 
- Lifecycle for Kotlin coroutines
- Coil for image loading
- Room for local database
- Navigation Component from Jetpack: for navigation between screens
- Jetpack Compose for UI
- Use of sealed classes get generic responses
- Use of Splash API