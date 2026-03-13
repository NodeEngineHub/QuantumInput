version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project

dependencies {
    // Use intra-build dependency to ensure local builds and publications align
    api(project(":quantum-action-api"))
    api("ca.nodeengine.quantum:quantum-api:0.1.0")

    api("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subNodePlugin {
    displayName = "QuantumInput Action"
    description = "The QuantumInput Action Core"
    includeJavadoc = true
}
