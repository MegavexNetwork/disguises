plugins {
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")
    implementation(project(":disguises", configuration = "reobf"))
}

paper {
    name = "disguises"
    main = "net.megavex.disguises.plugin.TestPlugin"
    apiVersion = "1.20"

    serverDependencies {
        register("ProtocolLib")
    }
}

tasks {
    runServer {
        minecraftVersion("1.20.2")
    }

    assemble {
        dependsOn(shadowJar)
    }
}
