plugins {
    `java-library`
    // https://kotlinlang.org/docs/releases.html
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
    // https://plugins.gradle.org/plugin/io.papermc.paperweight.userdev
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    // https://github.com/jpenilla/resource-factory
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.2.0"
}

group = "skin"
val pluginVersion = ""
val minecraftVersion = "1.21.4"
val javaVersion = 21
val filePath = ""

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    // https://github.com/Kotlin/kotlinx.serialization/releases
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
}

tasks {
    compileJava {
        options.release = javaVersion
    }
    jar {
        archiveFileName = "${rootProject.name}${pluginVersion.let { version -> if (version == "") version else "-${version}" }}.jar"
        if (filePath != "") {
            destinationDirectory.set(File(filePath))
        }
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(javaVersion)
}

kotlin {
    jvmToolchain(javaVersion)
}

paperPluginYaml {
    name = rootProject.name
    main = "$group.${rootProject.name}"
    version = pluginVersion.let { version -> if (version == "") "1.0.0" else version }
    apiVersion = minecraftVersion
    dependencies {
        server("Kotlin")
    }
}