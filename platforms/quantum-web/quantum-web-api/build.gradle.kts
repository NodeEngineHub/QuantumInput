version = "0.1.0"

val annotationsVersion: String by project
val lombokVersion: String by project
val junitVersion: String by project

// GraalVM Web Image interop API. compile-only: the native-image tool supplies the implementation at
// build time. The web platform's public API exposes JSObject (the canvas handle to register).
val webimagePreviewVersion = "25.1.3"

dependencies {
    api("ca.nodeengine.quantum:quantum-api:0.1.0")

    // GraalVM Web Image interop (JSObject). Zero deps, compile-only.
    compileOnly("org.graalvm.sdk:webimage-preview:$webimagePreviewVersion")

    api("org.jetbrains:annotations:$annotationsVersion")

    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    compileOnly("org.projectlombok:lombok:$lombokVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

subNodePlugin {
    displayName = "QuantumInput Web Platform API"
    description = "API for the QuantumInput GraalVM Web Image (DOM) platform"
}
