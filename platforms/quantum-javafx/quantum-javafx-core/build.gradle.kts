version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project
val javafxVersion: String by project

plugins {
    id("org.openjfx.javafxplugin") version "0.1.0"
}

javafx {
    version = javafxVersion
    modules("javafx.controls")
}

dependencies {
    api(project(":quantum-javafx-api"))
    api("ca.nodeengine.quantum:quantum-core:0.1.0")

    api("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subNodePlugin {
    displayName = "QuantumInput JavaFX Platform"
    description = "JavaFX Platform support for QuantumInput"
}
