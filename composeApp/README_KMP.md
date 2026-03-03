# KMP/CMP Run Guide

This project now includes a Compose Multiplatform module:
- `:composeApp` (Android + iOS)

## Android

Run:
```bash
./gradlew :composeApp:installDebug
```

## iOS (Xcode)

1. Ensure Xcode CLI tools are configured:
```bash
xcode-select -p
sudo xcode-select --switch /Applications/Xcode.app/Contents/Developer
sudo xcodebuild -runFirstLaunch
```

2. Install CocoaPods dependencies for iOS wrapper app:
```bash
cd iosApp
pod install
```

3. Open workspace in Xcode:
```bash
open iosApp.xcworkspace
```

4. Run the `iosApp` scheme on simulator/device.

## Useful tasks

Generate iOS framework:
```bash
./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64
```
