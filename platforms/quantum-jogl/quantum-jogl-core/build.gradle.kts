version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project
val joglVersion: String by project

dependencies {
    api(project(":quantum-jogl-api"))
    api("ca.nodeengine.quantum:quantum-core:0.1.0")

    api("org.jogamp.jogl:jogl-all:$joglVersion")
    api("org.jogamp.gluegen:gluegen-rt:$joglVersion")

    api("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    // For testing, we might need natives. 
    // JOGL 2.5.0 natives: linux-amd64, windows-amd64, macosx-universal, etc.
    // TODO: Add all other operating systems
    val joglNatives = "natives-linux-amd64" 
    testRuntimeOnly("org.jogamp.jogl:jogl-all:$joglVersion:$joglNatives")
    testRuntimeOnly("org.jogamp.gluegen:gluegen-rt:$joglVersion:$joglNatives")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subNodePlugin {
    displayName = "QuantumInput JOGL Platform"
    description = "JOGL Platform support for QuantumInput"
    includeJavadoc = true
}