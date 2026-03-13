pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    // Applies the foojay-resolver plugin to allow automatic download of JDKs.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "quantum-lwjgl3-sdl"

include(
    ":quantum-lwjgl3-sdl-api",
    ":quantum-lwjgl3-sdl-core"
)
includeBuild("../../")