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

rootProject.name = "quantum-jogl"

include(
    ":quantum-jogl-api",
    ":quantum-jogl-core"
)
includeBuild("../../")
