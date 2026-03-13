allprojects {
    group = "ca.nodeengine.quantum"
}

plugins {
    id("ca.nodeengine.node-plugin") version "1.1.7"
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.16.0"
}

nodePlugin {
    useProguard.set(false)
    publishApi.set(true)
    publishAll.set(true)
}
