import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.cocoapods) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.versions)
    alias(libs.plugins.spotless)
}

configure<SpotlessExtension> {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**", "**/.gradle/**", "iosApp/Pods/**")
        ktlint("1.7.1")
            .editorConfigOverride(
                mapOf(
                    "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
                    "max_line_length" to "120",
                    "ktlint_standard_filename" to "disabled",
                ),
            )
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**", "**/.gradle/**")
        ktlint("1.7.1")
    }
}

tasks.named<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
