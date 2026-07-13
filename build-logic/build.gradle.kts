plugins {
    `kotlin-dsl`
}

group = "com.travelbackintime.buildlogic"

dependencies {
    implementation("com.android.tools.build:gradle:8.11.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.21")
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "travelbackintime.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("jvmLibrary") {
            id = "travelbackintime.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
