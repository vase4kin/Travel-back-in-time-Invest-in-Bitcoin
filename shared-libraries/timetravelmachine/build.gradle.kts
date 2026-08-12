import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.cocoapods)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.detekt)
}

kotlin {
    android {
        namespace = "com.github.vase4kin.shared.timetravelmachine"
        compileSdk = 36
        minSdk = 23
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
        withHostTest {}
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.sharedLibraries.repository)
            implementation(libs.kotlinx.datetime)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }

    cocoapods {
        version = "1.1.3"
        summary = "Shared Bitcoin time travel domain logic"
        homepage = "https://github.com/vase4kin/Travel-back-in-time-Invest-in-Bitcoin"
        ios.deploymentTarget = "13.5"
        podfile = project.file("../../iosApp/Podfile")
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
