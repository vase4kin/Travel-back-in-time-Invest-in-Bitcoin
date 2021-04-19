/*
 * Copyright 2021  Andrey Tolpeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.buildSrc

object Config {
    const val minSdkVersion = 21
    const val compileSdkVersion = 30
    const val targetSdkVersion = 30
    const val buildToolsVersion = "30.0.2"
    const val versionCode = 19
    const val versionName = "1.1.3"
    const val applicationId = "com.travelbackintime.buybitcoin"
}

object KmmConfig {
    const val version = "1.0"
    const val deploymentTarget = "13.5"

    object Detekt {
        val sourcePaths = listOf(
            "src/commonMain/kotlin",
            "src/iOSMain/kotlin",
            "src/jvmMain/kotlin",
            "src/androidMain/kotlin"
        )
    }
}

object Libs {

    object Gradle {
        const val gradlePlugin = "com.android.tools.build:gradle:4.2.0-rc01"
    }

    object Google {
        const val googleServicesPlugin = "com.google.gms:google-services:4.3.5"

        object Firebase {
            const val bom = "com.google.firebase:firebase-bom:26.7.0"

            object Crashlytics {
                const val gradlePlugin = "com.google.firebase:firebase-crashlytics-gradle:2.5.2"
            }
        }

        object AndroidX {
            const val appCompat = "androidx.appcompat:appcompat:1.2.0"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
            const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
        }

        object Material {
            const val material = "com.google.android.material:material:1.3.0"
        }
    }

    object Dagger {
        private const val version = "2.29.1"

        const val dagger = "com.google.dagger:dagger:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object Detekt {
        const val version = "1.16.0"

        const val gradlePlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:$version"
        const val jvmTarget = "1.8"

        object Plugins {
            const val formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:$version"
        }
    }

    object Kotlin {
        internal const val version = "1.4.31"

        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$version"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2"

        object Plugins {
            const val gradle = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
            const val serialization = "org.jetbrains.kotlin:kotlin-serialization:$version"
        }
    }

    object Kmm {

        object Ktor {
            private const val version = "1.5.2"
            const val clientCore = "io.ktor:ktor-client-core:$version"
            const val clientSerialization = "io.ktor:ktor-client-serialization:$version"
            const val clientAndroid = "io.ktor:ktor-client-android:$version"
            const val clientIos = "io.ktor:ktor-client-ios:$version"
            const val logBack = "ch.qos.logback:logback-classic:1.2.3"
            const val clientLogging = "io.ktor:ktor-client-logging:$version"
        }

        object KotlinX {
            const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.1.1"
            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3-native-mt"
            const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1"
        }

        object Firebase {
            const val realtimeDatabase = "dev.gitlive:firebase-database:1.2.0"
        }
    }

    object Shared {
        const val tracker = ":shared-libraries:tracker"
        const val coinDeskService = ":shared-libraries:coindesk-service"
        const val repository = ":shared-libraries:repository"
        const val timeTravelMachine = ":shared-libraries:timetravelmachine"
        const val database = ":shared-libraries:database"
    }

    object Local {
        const val crashlytics = ":libraries:crashlytics"
        const val remoteConfig = ":libraries:remote-config"
    }

    object Test {
        const val jUnit = "junit:junit:4.13.2"
    }

    const val androidGifDrawable = "pl.droidsonroids.gif:android-gif-drawable:1.2.22"
    const val coreLibraryDesugaring = "com.android.tools:desugar_jdk_libs:1.1.5"
}