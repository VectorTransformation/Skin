plugins {
    `java-library`
    // https://kotlinlang.org/docs/releases.html
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.0"
    // https://plugins.gradle.org/plugin/io.papermc.paperweight.userdev
    id("io.papermc.paperweight.userdev") version "1.7.7"
}

group = "skin"
version = ""
val javaVersion = 21
val filePath = ""

dependencies {
    paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT")
    // https://github.com/Kotlin/kotlinx.serialization/releases
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0-RC")
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