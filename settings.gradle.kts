pluginManagement {
    if (java.nio.file.Files.isDirectory(File(rootDir, "./node-plugin").toPath())) {
        includeBuild("./node-plugin")
    }
    if (java.nio.file.Files.isDirectory(File(rootDir, "../node-plugin").toPath())) {
        includeBuild("../node-plugin")
    }
}

plugins {
    // Applies the foojay-resolver plugin to allow automatic download of JDKs.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "quantum-input"

include(":api", ":core")
