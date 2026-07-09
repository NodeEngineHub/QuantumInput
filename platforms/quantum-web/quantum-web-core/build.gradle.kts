version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project

// GraalVM Web Image interop API. compile-only: the native-image tool supplies the implementation at
// build time. Pinned to the 25.1 channel (matches the node-engine-web toolchain).
val webimagePreviewVersion = "25.1.3"

dependencies {
    api(project(":quantum-web-api"))
    api("ca.nodeengine.quantum:quantum-core:0.1.0")

    // GraalVM Web Image interop (@JS / JSObject). Zero deps, compile-only.
    compileOnly("org.graalvm.sdk:webimage-preview:$webimagePreviewVersion")

    api("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

// @JS snippet methods reference their parameters by name, which requires -parameters (otherwise every
// @JS would need an explicit args={...} list). Mirrors node-engine-web.
tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

subNodePlugin {
    displayName = "QuantumInput Web Platform"
    description = "GraalVM Web Image (DOM) platform support for QuantumInput"
    includeJavadoc = true
}
