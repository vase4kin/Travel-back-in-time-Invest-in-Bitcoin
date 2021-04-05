/*
 * Copyright 2020 Andrey Tolpeev
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

object Libs {
    object Kmm {
        object Ktor {
            private const val version = "1.5.2"
            const val clientCore = "io.ktor:ktor-client-core:$version"
            const val clientCio = "io.ktor:ktor-client-cio:$version"
            const val clientSerialization = "io.ktor:ktor-client-serialization:$version"
            const val clientAndroid = "io.ktor:ktor-client-android:$version"
            const val clientIos = "io.ktor:ktor-client-ios:$version"
            const val logBack = "ch.qos.logback:logback-classic:1.2.3"
            const val clientLogging = "io.ktor:ktor-client-logging:$version"
        }
        object KotlinX {
            const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:0.1.1"
        }
    }
    object Shared {
        const val tracker = ":shared-libraries:tracker"
        const val coinDeskService = ":shared-libraries:coindesk-service"
        const val repository = ":shared-libraries:repository"
        const val timeTravelMachine = ":shared-libraries:timetravelmachine"
        const val database = ":shared-libraries:database"
    }
}