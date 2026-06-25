# YouTube Video Search App

An Android app (Kotlin) that searches YouTube via the YouTube Data API v3 and
displays results in a RecyclerView.

## Features
- EditText + Search button (and keyboard "Search" action) for queries
- Retrofit network call to the YouTube Data API v3
- RecyclerView showing title, description, publish time, channel title, and thumbnail
- Glide for thumbnail loading
- ProgressBar shown while loading
- Error handling for empty input, network failure, and no results

## Tech
- Kotlin, Coroutines (off-main-thread networking)
- Retrofit + Gson
- Glide
- ViewBinding

## Setup
1. Open the project in Android Studio.
2. Let Gradle sync.
3. Run on a device or emulator with internet access.

The API key is set in `MainActivity.kt` (`apiKey`).
