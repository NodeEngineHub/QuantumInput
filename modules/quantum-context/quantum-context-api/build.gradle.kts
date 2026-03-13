version = "0.1.0"

val annotationsVersion: String by project
val junitVersion: String by project

dependencies {
    api("ca.nodeengine.quantum:quantum-api:0.1.0")
    api("ca.nodeengine.quantum:quantum-action-api:0.1.0")

    api("org.jetbrains:annotations:$annotationsVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subNodePlugin {
    displayName = "QuantumInput Context API"
    description = "The QuantumInput Context API"
}
