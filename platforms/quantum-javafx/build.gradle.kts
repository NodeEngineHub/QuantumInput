plugins {
    id("ca.nodeengine.node-plugin") version "1.2.0"
    `java-library`
    `maven-publish`
}

allprojects {
    group = "ca.nodeengine.quantum"
}
version = "0.1.0"

nodePlugin {
    useProguard.set(false)
    publishApi.set(true)
    publishAll.set(true)
    publishSonatype.set(true)
    vendor.set("FXCO Ltd.")
}

publishing {
    publications {
        create<MavenPublication>("root") {
            pom {
                name = "QuantumInput JavaFX Platform"
                description = "JavaFX Platform support for QuantumInput"
                inceptionYear = "2026"
                url = "https://github.com/NodeEngineHub/QuantumInput/"
                organization {
                    name = "NodeEngine"
                    url = "https://NodeEngine.ca/"
                }
                licenses {
                    license {
                        name = "LGPL-3.0-only"
                        url = "https://github.com/NodeEngineHub/QuantumInput/blob/master/LICENSE"
                        distribution = "repo"
                    }
                }
                developers {
                    developer {
                        id = "fxmorin"
                        name = "FX Morin"
                        url = "https://github.com/FxMorin/"
                    }
                }
                scm {
                    url = "https://github.com/NodeEngineHub/QuantumInput/"
                    connection = "scm:git:git://github.com/NodeEngineHub/QuantumInput.git"
                    developerConnection = "scm:git:ssh://git@github.com/NodeEngineHub/QuantumInput.git"
                }
                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/NodeEngineHub/QuantumInput/issues"
                }
            }
        }
    }
}
