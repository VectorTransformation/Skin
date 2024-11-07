plugins {
    `java-library`
    // https://github.com/JetBrains/kotlin/releases
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    // https://plugins.gradle.org/plugin/io.papermc.paperweight.userdev
    id("io.papermc.paperweight.userdev") version "1.7.4"
}

group = "skin"
version = ""
val javaVersion = 21
val filePath = ""

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    // https://github.com/Kotlin/kotlinx.serialization/releases
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

tasks {
    compileJava {
        options.release = javaVersion
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        expand(
            "name" to project.name,
            "pluginVersion" to if (version == "") "1.0.0" else version,
            "description" to "",
            "author" to "",
            "website" to "",
            "apiVersion" to "1.19.4"
        )
    }
    jar {
        archiveFileName = "${project.name}${if (version == "") version else "-${version}"}.jar"
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