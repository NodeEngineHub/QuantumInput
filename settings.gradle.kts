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

rootProject.name = "quantum"

include(
    ":quantum-api",
    ":quantum-core"
)
includeBuild("modules/quantum-action")
includeBuild("modules/quantum-context")
includeBuild("platforms/quantum-lwjgl3-glfw")
includeBuild("platforms/quantum-lwjgl3-sdl")
includeBuild("platforms/quantum-jinput")
includeBuild("platforms/quantum-jogl")
includeBuild("platforms/quantum-javafx")
includeBuild("platforms/quantum-jawt")
includeBuild("platforms/quantum-input4j")