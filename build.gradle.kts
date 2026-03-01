allprojects {
    group = "ca.nodeengine.quantum"
}

plugins {
    id("ca.nodeengine.node-plugin") version "1.0.4"
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.16.0"
}


publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            groupId = "ca.nodeengine.quantum"
            artifactId = "quantuminput"
            version = project.version.toString()

            pom {
                name.set("QuantumInput")
                description.set("QuantumInput is a modular, context-aware input system for Java")
                inceptionYear.set("2026")
                url.set("https://github.com/NodeEngineHub/QuantumInput")
                licenses {
                    license {
                        name.set("GNU Lesser General Public License v3.0")
                        url.set("https://github.com/NodeEngineHub/QuantumInput/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("fxmorin")
                        name.set("FX Morin")
                        url.set("https://github.com/fxmorin/")
                    }
                }
                scm {
                    url.set("https://github.com/NodeEngineHub/QuantumInput/")
                    connection.set("scm:git:git://github.com/NodeEngineHub/QuantumInput.git")
                    developerConnection.set("scm:git:ssh://git@github.com/NodeEngineHub/QuantumInput.git")
                }
            }
        }
    }
}

jreleaser {
    signing {
        active.set(org.jreleaser.model.Active.ALWAYS)
        armored.set(true)
    }
    deploy {
        maven {
            mavenCentral {
                register("sonatype") {
                    active.set(org.jreleaser.model.Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}

tasks.withType<org.jreleaser.gradle.plugin.tasks.JReleaserDeployTask>().configureEach {
    doFirst {
        val buildDir = project.layout.buildDirectory.get().asFile
        val jreleaserDir = File(buildDir, "jreleaser")
        if (!jreleaserDir.exists()) {
            jreleaserDir.mkdirs()
        }
    }
}
