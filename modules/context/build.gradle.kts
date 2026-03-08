plugins {
    id("ca.nodeengine.node-plugin") version "1.1.2"
    `java-library`
}

group = "ca.nodeengine.quantum.context"
version = "0.1.0"

nodePlugin {
    rootArtifactId.set("quantum-context")
    defaultArtifactId.set("|root|-|target|")
    useProguard.set(false)
    publishApi.set(true)
    publishAll.set(true)
}
