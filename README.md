# Moonthread Orrery

Turn three linked rings to reconstruct a lunar seal in a small, three-dimensional instrument. The thin hoops sit at different depths, with a crescent at the centre and a fixed point marking north. Three puzzles introduce the movement of the rings. A hint identifies the next ring to turn, and the game saves progress on the device.

## How to play

Read the three directions above the instrument. **Ash** turns its pointer and Tide’s pointer clockwise. **Tide** turns its own pointer and Ember’s. **Ember** moves alone. Match all three directions to light the seal. You can reset the current seal or ask for a hint. The buttons include the compass value of every pointer, so you can play without relying on colour.

For the first seal, turn Ash once, then Tide once. The pointers reach east, south and east. The next seal starts in a different position.

## Build and install

The downloadable development APK passed the Android build, puzzle tests, signature verification and package identifier checks. The author’s Windows device blocked the unsigned PICO 6.2.1 emulator before it could start, so this preview still needs a playthrough on the official emulator. [Download the APK and SHA-256 checksum](https://github.com/MarcoDiPolo/moonthread-orrery/releases/tag/v0.1.0-preview) · [Build results](https://github.com/MarcoDiPolo/moonthread-orrery/actions/runs/45553123085)

The Android project targets PICO OS 6 and the official PICO emulator. It uses the PICO Spatial SDK 6.2.8, Kotlin 2.1.20, Android API 35, Android Gradle Plugin 8.13.2 and Gradle 8.13. Use a compatible Java 17 or 21 JDK and install the Android SDK Platform 35, Build Tools 35.0.0 and Platform Tools. Point `ANDROID_HOME` to your SDK.

On Windows, run `gradlew.bat :app:testDebugUnitTest :app:assembleDebug`. On macOS or Linux, run `sh gradlew :app:testDebugUnitTest :app:assembleDebug`. The result is `app/build/outputs/apk/debug/app-debug.apk`, an installable development APK for arm64.

Launch the official PICO emulator. In a terminal with its `adb` on the path, install with `adb install -r app/build/outputs/apk/debug/app-debug.apk`. Open **Moonthread Orrery** from the launcher or run `adb shell am start -n studio.moonthread.orrery/.platform.LaunchActivity`. [PICO emulator guide](https://developer.picoxr.com/document/spatial-toolkit/install-pico-emulator/)

## Design and verification

All visible 3D geometry is built at runtime using the PICO SDK’s native meshes, transforms and materials. Compose panels live inside the volumetric window. The project needs no service account or external AI service. Puzzle tests check the coupled turns and that the hint solves each seal from all 64 possible starting positions.

The official PICO CLI generated the volumetric project during the contest. OpenAI Codex assisted with design, Kotlin implementation, the original launcher artwork and testing. The intended competition track is Android/Kotlin. Future work includes controller gestures for turning a ring directly and more constellations.

The original puzzle and artwork are MIT licensed. The Gradle wrapper and platform dependencies retain their respective terms. [Third-party notes](THIRD_PARTY.md)
