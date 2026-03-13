version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project
val input4jVersion: String by project

dependencies {
    api(project(":quantum-input4j-api"))
    api("ca.nodeengine.quantum:quantum-core:0.1.0")

    api("de.gurkenlabs:input4j:$input4jVersion")

    api("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subNodePlugin {
    displayName = "QuantumInput Input4J Platform"
    description = "Input4J Platform support for QuantumInput"
    includeJavadoc = true
}
