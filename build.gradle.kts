import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.6.1"
}

group = "com.github.treemanking.budgiesets"
version = "1.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

repositories {
    mavenLocal()
    maven ("https://repo.papermc.io/repository/maven-public/")
    maven ("https://oss.sonatype.org/content/groups/public/")
    maven ("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.maven.apache.org/maven2/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("dev.jorel:commandapi-paper-shade:11.1.0")
    implementation("de.tr7zw:item-nbt-api:2.16.0")
    compileOnly("me.clip:placeholderapi:2.12.2")
}

tasks.jar {
    enabled = false
}

tasks.withType<ShadowJar> {
    archiveFileName.set("${project.name}-${project.version}.jar")
    relocate("de.tr7zw.changeme.nbtapi", "com.github.treemanking.api.nbtapi")
    relocate("dev.jorel.commandapi", "com.github.treemanking.api.commandapi")
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
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

tasks.register<Copy>("publishLocal") {
    group = "budgienet"
    description = "Copies the built plugin jar to the local Paper server's plugins folder."
    dependsOn(tasks.named("shadowJar"))

    val shadowJarTask = tasks.named<Jar>("shadowJar").get()
    from(shadowJarTask.archiveFile)
    into("/data/BudgieNet/PAPER_1_21_11/plugins/")
}

tasks.register<Exec>("restartPaper") {
    group = "budgienet"
    description = "Kills any running paper.jar process so the server can restart."

    isIgnoreExitValue = true
    commandLine("pkill", "-f", "paper.jar")
}