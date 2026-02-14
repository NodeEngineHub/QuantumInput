plugins {
    id("NodePlugin")
    `java-library`
}

group = "ca.nodeengine.quantum.action"
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
