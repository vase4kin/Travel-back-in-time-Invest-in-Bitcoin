pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TravelBackInTimeBuyBitcoin"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":libraries:remote-config")
include(":libraries:crashlytics")
include(":shared-libraries:bitcoin-price-service")
include(":shared-libraries:repository")
include(":shared-libraries:timetravelmachine")
include(":shared-libraries:tracker")
