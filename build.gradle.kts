allprojects {
    group = "ca.nodeengine.quantum"
}

plugins {
    id("ca.nodeengine.node-plugin") version "1.1.2"
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.16.0"
}

nodePlugin {
    rootArtifactId.set("quantum")
    defaultArtifactId.set("|root|-|target|")
    useProguard.set(false)
    publishApi.set(true)
    publishAll.set(true)
}
