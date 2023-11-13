plugins {
    kotlin("jvm") version "1.9.20" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "net.megavex"
    version = "0.1.0-SNAPSHOT"

    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.dmulloy2.net/repository/public/")
    }
}
