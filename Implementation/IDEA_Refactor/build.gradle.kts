plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.8.21"
    id("org.jetbrains.intellij") version "1.13.3"
//    id("org.jetbrains.intellij") version "1.8.0"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    maven { url = uri("https://plugins.jetbrains.com/maven") }
    mavenCentral()
    maven { url = uri("https://maven.aliyun.com/nexus/content/groups/public/") }
    maven { url = uri("https://maven.aliyun.com/repository/google")  }
    maven { url = uri("https://maven.aliyun.com/nexus/content/repositories/jcenter") }
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }

    mavenLocal()
    google()}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.2.5")
//    version.set("2023.2.4")
    type.set("IC") // Target IDE Platform

    // plugins.set(listOf(/* Plugin Dependencies */))
    plugins.set(arrayListOf("java", "gradle"))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    patchPluginXml {
        sinceBuild.set("222")
        untilBuild.set("232.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }
}

dependencies {
    implementation("org.json:json:20210307")
}

