allprojects {
    group = "ca.nodeengine.quantum"
}

plugins {
    id("NodePlugin")
    id("com.vanniktech.maven.publish") version "0.35.0"
}

mavenPublishing {
    coordinates("$group", "intellijreactflow", "$version")

    pom {
        name.set("Intellij React Flow")
        description.set("An intellij plugin library, which provides the ability to use React Flow within Intellij")
        inceptionYear.set("2025")
        url.set("https://github.com/NodeEngineHub/IntellijReactFlow")
        licenses {
            license {
                name.set("GNU Lesser General Public License v3.0")
                url.set("https://github.com/NodeEngineHub/IntellijReactFlow/blob/master/LICENSE")
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
            url.set("https://github.com/NodeEngineHub/IntellijReactFlow/")
            connection.set("scm:git:git://github.com/NodeEngineHub/IntellijReactFlow.git")
            developerConnection.set("scm:git:ssh://git@github.com/NodeEngineHub/IntellijReactFlow.git")
        }
    }
    publishToMavenCentral()
    signAllPublications()
}
