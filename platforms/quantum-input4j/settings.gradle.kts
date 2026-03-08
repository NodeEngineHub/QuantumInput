pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // Applies the foojay-resolver plugin to allow automatic download of JDKs.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "quantum-input4j"

include(
    ":quantum-input4j-api",
    ":quantum-input4j-core"
)
includeBuild("../../")
