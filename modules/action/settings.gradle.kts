pluginManagement {
    includeBuild("../../NodePlugin")
}

plugins {
    // Applies the foojay-resolver plugin to allow automatic download of JDKs.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "action"

includeOptionalComposite("../../", "ca.nodeengine.quantum")
include(
    ":api",
    ":core"
)

fun Settings.includeOptionalComposite(name: String, group: String) {
    val path = File(rootDir, name).toPath()
    if (java.nio.file.Files.exists(path) && java.nio.file.Files.isDirectory(path)) {
        includeBuild(name)
    }
}