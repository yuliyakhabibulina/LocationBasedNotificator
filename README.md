# Location Based Notificator
A mobile application that allows users to create location-based memos and reminders which trigger automatically when the user reaches a specified location.

# Features

Create Memos with Location:
Users can create a new memo by selecting a specific point on a map (Google Maps or OpenStreetMap).

# Smart Notifications:
When the user comes within 200 meters of the saved location, the app displays a notification in the phone’s status bar.

# Persistent Functionality:
The location tracking and notifications continue to work:
When the app is running in the background, or
Even when the app is not running at all

# How It Works

User creates a new memo and selects a location on the map.
The memo (title, content, and coordinates) is saved.
The app monitors the user’s location.
Once the user enters a 200m radius of the saved location, a notification is show

# Build & Run environment:
Android Studio Narwhal 3 Feature Drop | 2025.1.3
Gradle version: 8.12.2
Kotlin version: 2.2.10
JDK version: java 21.0.7 2025-04-15 LTS
target SDK: Android API level 36
min SDK: Android API level 26

# Tech stack:
- Kotlin
- Coroutines
- Jetpack Navigation Component
- Hilt
- Kotlinx serialization
- Room 
- Flow

# Testing:
- Robolectric
- Mockito
- JUnit
- Turbine

# Other
- Google Play Services Location (Geofencing) & Maps SDK

# Architecture:
- Clean Architecture
- MVVM
