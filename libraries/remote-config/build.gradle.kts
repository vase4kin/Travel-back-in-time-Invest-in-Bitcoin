plugins {
    id("travelbackintime.jvm.library")
    alias(libs.plugins.detekt)
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    autoCorrect = false
}

dependencies {
    testImplementation(libs.junit4)
    detektPlugins(libs.detekt.formatting)
}
