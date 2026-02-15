pluginManagement {
    includeBuild("../../NodePlugin")
}

plugins {
    // Applies the foojay-resolver plugin to allow automatic download of JDKs.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "quantum-jogl"

includeOptionalComposite("../../", "ca.nodeengine.quantum")
include(
    ":api",
    ":core"
)

fun Settings.includeOptionalComposite(name: String, group: String) {
    val path = file(name)
    if (path.exists() && path.isDirectory) {
        includeBuild(name) {
            dependencySubstitution {
                substitute(module("$group:api")).using(project(":api"))
                substitute(module("$group:core")).using(project(":core"))
            }
        }
    }
}
