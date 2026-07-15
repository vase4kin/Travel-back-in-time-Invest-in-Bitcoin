import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.android.library)
    alias(libs.plugins.detekt)
}

kotlin {
    androidTarget {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    cocoapods {
        version = "1.1.3"
        summary = "Shared analytics contracts"
        homepage = "https://github.com/vase4kin/Travel-back-in-time-Invest-in-Bitcoin"
        ios.deploymentTarget = "13.5"
    }
}

android {
    namespace = "com.github.vase4kin.shared.tracker"
    compileSdk = 36
    defaultConfig.minSdk = 23
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    source.setFrom(files("src/commonMain/kotlin", "src/commonTest/kotlin"))
    autoCorrect = false
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}
