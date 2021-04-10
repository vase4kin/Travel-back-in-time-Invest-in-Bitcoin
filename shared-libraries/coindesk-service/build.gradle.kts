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

import app.buildSrc.Libs
import app.buildSrc.KmmConfig

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("io.gitlab.arturbosch.detekt")
    id ("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    android()
    ios {}

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Libs.Kmm.Ktor.clientCore)
                implementation(Libs.Kmm.Ktor.clientSerialization)
                implementation(Libs.Kmm.Ktor.logBack)
                implementation(Libs.Kmm.Ktor.clientLogging)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Libs.Kmm.Ktor.clientAndroid)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(Libs.Kmm.Ktor.clientIos)
            }
        }
    }

    cocoapods {
        summary = "Shared coindesk service library"
        homepage ="home page"

        ios.deploymentTarget = KmmConfig.deploymentTarget
    }
}

android {
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}

detekt {
    input = files(
        "src/commonMain/kotlin",
        "src/iOSMain/kotlin",
        "src/jvmMain/kotlin"
    )
}