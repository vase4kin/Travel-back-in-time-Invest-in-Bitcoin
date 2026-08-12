[![License](https://img.shields.io/badge/license-Apache_2.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![CI](https://github.com/vase4kin/Travel-back-in-time-Invest-in-Bitcoin/actions/workflows/build.yml/badge.svg)](https://github.com/vase4kin/Travel-back-in-time-Invest-in-Bitcoin/actions/workflows/build.yml)
[![Android](https://img.shields.io/badge/android-1.1.3-blue.svg)](https://github.com/vase4kin/Back-in-time-Buy-bitcoin/releases/latest)

<img src="https://github.com/vase4kin/Back-in-time-Buy-bitcoin/raw/main/screenshots/feature-graphic.png" alt="Travel back in time: Invest in Bitcoin">

# Travel back in time: Invest in Bitcoin

Travel back to any supported date, enter a hypothetical USD investment, and see what that Bitcoin would be worth today. This repository contains the Android application and its Kotlin Multiplatform domain/data implementation, which is also consumed by the iOS SwiftUI application.

## Architecture

- Android UI: a single `ComponentActivity` with Jetpack Compose, Material 3, Navigation 3, immutable `StateFlow` UI state, lifecycle-aware collection, and Hilt-injected ViewModels.
- Shared domain: `shared-libraries/timetravelmachine` owns the calculation and exposes the stable API used by Android and Swift.
- Shared data: `shared-libraries/repository` separates domain code from the network provider.
- Network: `shared-libraries/bitcoin-price-service` uses Ktor and Kotlin serialization to obtain current and historical BTC/USD prices from Blockchain.com.
- Cross-platform targets: Android, iOS arm64, iOS Simulator arm64, and iOS Simulator x64.
- Build: Gradle Kotlin DSL, a version catalog, local convention plugins in `build-logic`, and JDK 17 toolchains.

UI events flow to `TimeTravelViewModel`; immutable state flows back to stateless Compose screens. Network DTOs stay in the price-service module, repository mapping stays in the repository module, and calculation behavior stays in the shared domain module.

## Requirements

- JDK 17 or newer
- Android SDK with API 36
- Xcode and CocoaPods for the iOS app

## Build and verify

```shell
./gradlew assembleDebug
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew detekt
./gradlew spotlessCheck
./gradlew :app:compileDebugAndroidTestKotlin
```

Run the Compose UI tests with a supported connected device or emulator:

```shell
./gradlew :app:connectedDebugAndroidTest
```

Build the Kotlin iOS simulator framework and Swift app:

```shell
./gradlew podInstall
cd iosApp
sh build.sh
```

## Project layout

- `app/`: Android Compose application, navigation, ViewModel, DI, Firebase, ads, and Android tests.
- `libraries/`: JVM-only abstractions for crash reporting and remote configuration.
- `shared-libraries/bitcoin-price-service/`: KMP network client and provider DTOs.
- `shared-libraries/repository/`: KMP repository boundary and iOS composition root.
- `shared-libraries/timetravelmachine/`: KMP Bitcoin investment domain calculation.
- `shared-libraries/tracker/`: shared analytics contract.
- `iosApp/`: SwiftUI application and CocoaPods workspace.
- `build-logic/`: reusable local Gradle convention plugins.
- `gradle/libs.versions.toml`: dependency and plugin version source of truth.

## License

```text
Copyright 2021 Andrey Tolpeev

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
