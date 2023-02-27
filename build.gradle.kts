plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version ("7.1.2")
    id("io.papermc.paperweight.userdev") version "1.3.5"
}

configure<JavaPluginExtension> {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

group = "me.zhacked"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://jitpack.io")
    maven("https://repo.unnamed.team/repository/unnamed-public/")
    maven("https://papermc.io/repo/repository/maven-public/")
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.kryptonmc.org/releases")
    maven("https://repo.minebench.de/")
}

dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.google.inject:guice:5.1.0")
    compileOnly("org.spongepowered:configurate-yaml:4.1.2")
    compileOnly("me.neznamy", "tab-api", "3.2.4")
    compileOnly("redis.clients:jedis:4.3.1")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.14.0-rc3")
    implementation("com.github.YoSoyVillaa.VMessenger:redis:1.1.4")
    implementation("com.github.YoSoyVillaa.VMessenger:universal:1.1.4")
    compileOnly(fileTree("libs/"))
}

tasks {
    build {
        dependsOn(reobfJar)
    }
}