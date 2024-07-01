import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

val funcName = "putfunname"
val group = "cn.cyanbukkit.${funcName}"
val version = "0.1"
val mainPlugin = "SiModuleGame"

bukkit {
    name = rootProject.name
    description = "put fun name here"
    authors = listOf("Your Name")
    website = "https://cyanbukkit.cn"
    main = "${group}.cyanlib.launcher.CyanPluginLauncher"
    loadBefore = listOf(mainPlugin)
}

plugins {
    java
    kotlin("jvm") version "1.9.20"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

repositories {
    maven("https://nexus.cyanbukkit.cn/repository/maven-public/")
    maven("https://maven.elmakers.com/repository")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly(fileTree("libs") { include("*.jar") })

}

kotlin {
    jvmToolchain(8)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    jar {
        archiveFileName.set("${rootProject.name}-${version}.jar")
    }
    build {
        doLast {
            val shadowJarFile = File("${rootProject.buildDir}/libs/${rootProject.name}-${version}.jar")
            uploadTo(shadowJarFile)
        }
    }
}





fun uploadTo(shadowJarFile: File) {
    val s = "https://api.cyanbukkit.cn/v1/live/game/upload?name=${rootProject.name}&version=${version}"
    val url = URL(s).openConnection() as HttpsURLConnection
    url.setRequestProperty("Content-Type", "application/java-archive")
    url.setRequestProperty("x-token", properties["token"].toString())
    println("start upload ")
    url.requestMethod = "PUT"
    url.doOutput = true
    try {
        url.outputStream.use { output ->
            shadowJarFile.inputStream().use { input ->
                input.copyTo(output)
            }
        }
    } catch (e: Exception) {
        println("Error during file transfer: ${e.message}")
    }
    println("uploading")
    if (url.responseCode != 200) {
        throw IOException(url.content.toString())
    } else {
        println("upload success")
    }
}