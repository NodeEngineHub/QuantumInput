version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project
val lwjglVersion: String by project

dependencies {
    api(project(":quantum-lwjgl3-glfw-api"))
    api("ca.nodeengine.quantum:quantum-core:0.1.0")

    api("org.lwjgl:lwjgl:$lwjglVersion")
    api("org.lwjgl:lwjgl-glfw:$lwjglVersion")

    api("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    val lwjglNatives = "natives-linux" // Simplified for this environment
    testRuntimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    testRuntimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subNodePlugin {
    displayName = "QuantumInput LWJGL3 GLFW Platform"
    description = "LWJGL3 GLFW Platform support for QuantumInput"
    includeJavadoc = true
}
