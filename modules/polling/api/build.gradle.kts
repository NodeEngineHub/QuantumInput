version = "0.1.0"

val annotationsVersion: String by project
val junitVersion: String by project

dependencies {
    api("ca.nodeengine.quantum:api:0.1.0")

    // Runtime dependency for QuantumInput core (only API types are used at compile time)
    runtimeOnly("ca.nodeengine.quantum:core:0.1.0")

    api("org.jetbrains:annotations:$annotationsVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}
