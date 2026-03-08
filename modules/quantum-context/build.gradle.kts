plugins {
    id("ca.nodeengine.node-plugin") version "1.1.2"
    `java-library`
}

group = "ca.nodeengine.quantum"
version = "0.1.0"

nodePlugin {
    useProguard.set(false)
    publishApi.set(true)
    publishAll.set(true)
}
