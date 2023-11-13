plugins {
    id("io.papermc.paperweight.userdev") version "1.5.5"
}

dependencies {
    paperweight.paperDevBundle("1.20.2-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

kotlin {
    explicitApi()
}
