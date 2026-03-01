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

rootProject.name = "QuantumInput"

include(
    ":api",
    ":core"
)
includeBuild("modules/action") {
    dependencySubstitution {
        substitute(module("ca.nodeengine.quantum.action:api")).using(project(":api"))
        substitute(module("ca.nodeengine.quantum.action:core")).using(project(":core"))
    }
}
includeBuild("modules/context") {
    dependencySubstitution {
        substitute(module("ca.nodeengine.quantum.context:api")).using(project(":api"))
        substitute(module("ca.nodeengine.quantum.context:core")).using(project(":core"))
    }
}
includeBuild("platforms/LWJGL3-GLFW")
includeBuild("platforms/LWJGL3-SDL")
includeBuild("platforms/JInput")
includeBuild("platforms/JOGL")
includeBuild("platforms/JavaFX")
includeBuild("platforms/JAWT")
includeBuild("platforms/Input4j")
