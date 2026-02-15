version = "0.1.0"

val annotationsVersion: String by project

dependencies {
    api("ca.nodeengine.quantum:api:0.1.0")
    api("org.jetbrains:annotations:$annotationsVersion")
}
