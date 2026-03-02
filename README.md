# SeatMap

A Jetpack Compose Android app that renders an interactive aircraft seat map with zoom/pan, seat selection, confirmation flow, and a boarding pass screen.

## Features

- Interactive seat map canvas with pinch-to-zoom and pan
- Seat selection with animated details card
- Confirmation animation into boarding pass ticket view
- Modularized Compose code for separation of concerns and reusability

## Project Structure

- `app/src/main/java/com/example/seatmap/MainActivity.kt`: App entry point
- `app/src/main/java/com/example/seatmap/SeatMapScreen.kt`: Main screen/state orchestration
- `app/src/main/java/com/example/seatmap/SeatMapCanvas.kt`: Aircraft and seat drawing logic
- `app/src/main/java/com/example/seatmap/SeatLayoutGenerator.kt`: Deterministic seat layout generation
- `app/src/main/java/com/example/seatmap/SeatModels.kt`: Domain models and enums
- `app/src/main/java/com/example/seatmap/SeatInfoComponents.kt`: Reusable seat details components
- `app/src/main/java/com/example/seatmap/BoardingPassTicket.kt`: Boarding pass UI
- `app/src/main/java/com/example/seatmap/FloatingHeader.kt`: Expandable flight header component

## Requirements

- Android Studio (latest stable recommended)
- JDK 17+
- Android SDK configured in your environment

## Build

```bash
./gradlew :app:compileDebugKotlin
```

## Run

1. Open the project in Android Studio.
2. Sync Gradle.
3. Run the `app` configuration on an emulator/device.

## Notes

- Seat occupancy/layout generation uses fixed random seeds for stable behavior.
- Current default branch on remote may still be `main`; code is currently pushed on `master`.
