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

rootProject.name = "quantum-lwjgl3-glfw"

include(
    ":quantum-lwjgl3-glfw-api",
    ":quantum-lwjgl3-glfw-core"
)
includeBuild("../../")