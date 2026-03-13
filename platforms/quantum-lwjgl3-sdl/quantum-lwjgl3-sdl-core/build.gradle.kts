version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project
val lwjglVersion: String by project

repositories {
    mavenCentral()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {
    api(project(":quantum-lwjgl3-sdl-api"))
    api("ca.nodeengine.quantum:quantum-core:0.1.0")

    api("org.lwjgl:lwjgl:$lwjglVersion")
    api("org.lwjgl:lwjgl-sdl:$lwjglVersion")

    api("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    // TODO: Add other operating systems
    val lwjglNatives = "natives-linux" // Simplified for this environment
    testRuntimeOnly("org.lwjgl:lwjgl:3.4.1:$lwjglNatives")
    testRuntimeOnly("org.lwjgl:lwjgl-sdl:3.4.1:$lwjglNatives")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subNodePlugin {
    displayName = "QuantumInput LWJGL3 SDL Platform"
    description = "LWJGL3 SDL Platform support for QuantumInput"
}
