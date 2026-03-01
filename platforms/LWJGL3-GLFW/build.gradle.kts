plugins {
    id("ca.nodeengine.node-plugin") version "1.0.4"
    `java-library`
}

group = "ca.nodeengine.quantum.platform.lwjgl3-glfw"
version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project
val fastutilVersion: String by project

dependencies {
    api("org.jetbrains:annotations:$annotationsVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
