pluginManagement {
    includeBuild("./NodePlugin")
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
includeBuild("modules/action")
includeBuild("modules/context")
includeBuild("platforms/LWJGL3-GLFW")
