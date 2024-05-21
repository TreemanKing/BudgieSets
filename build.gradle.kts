import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.github.treemanking.budgiesets"
version = "1.0.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation(libs.commandapi.bukkit.shade)
    implementation(libs.de.tr7zw.item.nbt.api)
    compileOnly(libs.io.papermc.paper.paper.api)
    compileOnly(libs.me.clip.placeholderapi)

}

tasks.withType<ShadowJar> {
    archiveFileName.set("${project.name}-${project.version}.jar")
    relocate("de.tr7zw.changeme.nbtapi", "com.github.treemanking.budgiesets.nbtapi")
    relocate("dev.jorel.commandapi", "com.github.treemanking.budgiesets.commandapi")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("projectVersion" to project.version)
    }
}