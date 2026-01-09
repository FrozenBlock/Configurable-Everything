import groovy.xml.XmlSlurper
import org.codehaus.groovy.runtime.ResourceGroovyMethods
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.kohsuke.github.GHReleaseBuilder
import org.kohsuke.github.GitHub
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.nio.file.Files
import java.util.*

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.kohsuke:github-api:1.316")
    }
}

plugins {
    kotlin("jvm") version("2.3.0")
    id("fabric-loom") version("1.14-SNAPSHOT")
    id("dev.yumi.gradle.licenser") version("+")
    id("org.ajoberstar.grgit") version("+")
    id("com.modrinth.minotaur") version("+")
    id("com.gradleup.shadow") version("+")
    eclipse
    idea
    `java-library`
    java
    `maven-publish`
}

val minecraft_version: String by project
val quilt_mappings: String by project
val parchment_mappings: String by project
val loader_version: String by project

val mod_id: String by project
val mod_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val fabric_version: String by project
val fabric_kotlin_version: String by project
val frozenlib_version: String by project

val cloth_config_version: String by project
val modmenu_version: String by project

val sodium_version: String by project

base {
    archivesName.set(archives_base_name)
}

version = getModVersion()
group = maven_group

val local_frozenlib = findProject(":FrozenLib") != null
val release = findProperty("releaseType")?.equals("stable")

loom {
    runtimeOnlyLog4j.set(true)

    accessWidenerPath.set(file("src/main/resources/$mod_id.classtweaker"))
    interfaceInjection {
        // When enabled, injected interfaces from dependencies will be applied.
        enableDependencyInterfaceInjection.set(false)
    }
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/generated")
        }
    }
}

loom {
    runs {
        register("datagen") {
            client()
            name("Data Generation")
            vmArg("-Dfabric-api.datagen")
            vmArg("-Dfabric-api.datagen.output-dir=${file("src/main/generated")}")
            // vmArg("-Dfabric-api.datagen.strict-validation")
            vmArg("-Dfabric-api.datagen.modid=$mod_id")

            ideConfigGenerated(true)
            runDir = "build/datagen"
        }

        named("client") {
            ideConfigGenerated(true)
        }
        named("server") {
            ideConfigGenerated(true)
        }
    }
}

val includeModApi: Configuration by configurations.creating
val includeImplementation: Configuration by configurations.creating
val shadowInclude: Configuration by configurations.creating

configurations {
    include {
        extendsFrom(includeImplementation)
        extendsFrom(includeModApi)
    }
    implementation {
        extendsFrom(includeImplementation)
    }
    modApi {
        extendsFrom(includeModApi)
    }
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    maven("https://jitpack.io")
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"

        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.terraformersmc.com") {
        content {
            includeGroup("com.terraformersmc")
        }
    }
    maven("https://maven.shedaniel.me/")
    /*maven {
        name = "Siphalor's Maven"
        url = uri("https://maven.siphalor.de")
    }*/
    maven("https://maven.flashyreese.me/releases")
    maven("https://maven.flashyreese.me/snapshots")
    maven("https://maven.minecraftforge.net/")
    //maven("https://maven.parchmentmc.org")
    maven("https://maven.quiltmc.org/repository/release") {
        name = "Quilt"
    }
    maven("https://maven.jamieswhiteshirt.com/libs-release") {
        content {
            includeGroup("com.jamieswhiteshirt")
        }
    }
    maven("https://maven.frozenblock.net/release") {
        name = "FrozenBlock"
    }

    flatDir {
        dirs("libs")
    }
    mavenCentral()
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings(
        loom.layered {
            // please annoy treetrain if this doesnt work
            //mappings("org.quiltmc:quilt-mappings:${quilt_mappings}:intermediary-v2")
            //parchment("org.parchmentmc.data:parchment-${parchment_mappings}@zip")
            officialMojangMappings {
                nameSyntheticMembers = false
            }
        }
    )
    modImplementation("net.fabricmc:fabric-loader:${loader_version}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${fabric_version}")

    // Fabric Language Kotlin. Required for Kotlin support.
    modImplementation("net.fabricmc:fabric-language-kotlin:${fabric_kotlin_version}")

    // Kotlin Metadata Remapping
    api(files("libs/fabric-loom-1.14.local-kotlin-remapper.jar"))?.let { shadowInclude(it) }

    // get deps manually because FKE cant give them to compile classpath without an error
    api(kotlin("scripting-common"))
    api(kotlin("scripting-jvm"))
    api(kotlin("scripting-jsr223"))
    api(kotlin("scripting-jvm-host"))
    api(kotlin("scripting-compiler-embeddable"))
    api(kotlin("scripting-dependencies"))
    api(kotlin("scripting-dependencies-maven"))

    api("net.fabricmc:mapping-io:0.8.0")
    api("net.fabricmc:tiny-remapper:0.12.0")

    // FrozenLib
    if (local_frozenlib) {
        api(project(":FrozenLib", configuration = "namedElements"))
        modCompileOnly(project(":FrozenLib"))
    }
    else {
        modApi("maven.modrinth:frozenlib:$frozenlib_version")
    }

    // Cloth Config
    modApi("me.shedaniel.cloth:cloth-config-fabric:${cloth_config_version}") {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(group = "com.terraformersmc")
    }

    // Mod Menu
    modImplementation("com.terraformersmc:modmenu:${modmenu_version}")

    // Sodium
    modCompileOnly("maven.modrinth:sodium:$sodium_version")
}

tasks {
    processResources {
        val properties = HashMap<String, Any>()
        properties["mod_id"] = mod_id
        properties["version"] = version
        properties["minecraft_version"] = minecraft_version
        properties["fabric_kotlin_version"] = ">=$fabric_kotlin_version"

        properties.forEach { (a, b) -> inputs.property(a, b) }

        filesNotMatching(
            listOf(
                "**/*.java",
                "**/*.kt",
                "**/sounds.json",
                "**/lang/*.json",
                "**/.cache/*",
                "**/*.accesswidener",
                "**/*.classtweaker",
                "**/*.nbt",
                "**/*.png",
                "**/*.ogg",
                "**/*.mixins.json"
            )
        ) {
            expand(properties)
        }
    }

    //license {
    //    rule(project.file("codeformat/HEADER"))

    //    include("**/*.java")
    //}

    test {
        useJUnitPlatform()
    }

    shadowJar {
        configurations = listOf(shadowInclude)
        enableAutoRelocation = true
        relocationPrefix = "net.frozenblock.configurableeverything.shadow"
    }

    register("javadocJar", Jar::class) {
        dependsOn(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc.get().destinationDir)
    }

    register("sourcesJar", Jar::class) {
        dependsOn(classes)
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    remapJar {
        dependsOn(shadowJar)
        input = shadowJar.get().archiveFile
    }

    withType(JavaCompile::class) {
        options.encoding = "UTF-8"
        // Minecraft 1.20.5 (24w14a) upwards uses Java 21.
        options.release.set(21)
        options.isFork = true
        options.isIncremental = true
    }

    withType(KotlinCompile::class) {
        compilerOptions {
            // Minecraft 1.20.5 (24w14a) upwards uses Java 21.
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    withType(Test::class) {
        maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
    }
}

val test: Task by tasks
val runClient: Task by tasks
val runDatagen: Task by tasks

val remapJar: Task by tasks
val sourcesJar: Task by tasks
val javadocJar: Task by tasks

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}

tasks {
    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName}" }
        }
    }
}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

fun getModVersion(): String {
    var version = "$mod_version-mc$minecraft_version"

    //if (release != null && !release) {
    //    version += "-unstable"
    //}

    return version
}

if (!(release == true || System.getenv("GITHUB_ACTIONS") == "true")) {
    test.dependsOn(runDatagen)
    runClient.dependsOn(runDatagen)
}

val env: MutableMap<String, String> = System.getenv()

publishing {
    val mavenUrl = env["MAVEN_URL"]
    val mavenUsername = env["MAVEN_USERNAME"]
    val mavenPassword = env["MAVEN_PASSWORD"]

    val release = mavenUrl?.contains("release")
    val snapshot = mavenUrl?.contains("snapshot")

    val publishingValid = rootProject == project &&
        !mavenUrl.isNullOrEmpty() &&
        !mavenUsername.isNullOrEmpty() &&
        !mavenPassword.isNullOrEmpty()

    val publishVersion = makeModrinthVersion(mod_version)
    val snapshotPublishVersion = publishVersion + if (snapshot == true) "-SNAPSHOT" else ""

    val publishGroup = rootProject.group.toString().trim(' ')

    val hash = if (grgit.branch != null && grgit.branch.current() != null) grgit.branch.current().fullName else ""

    publications {
        var publish = true
        if (publishingValid) {
            try {
                try {
                    val xml = ResourceGroovyMethods.getText(
                        uri(
                            "$mavenUrl/${publishGroup.replace('.', '/')}/$snapshotPublishVersion/$publishVersion.pom"
                        ).toURL()
                    )
                    val metadata = XmlSlurper().parseText(xml)

                    if (metadata.getProperty("hash").equals(hash)) {
                        publish = false
                    }
                } catch (ignored: FileNotFoundException) {
                    // No existing version was published, so we can publish
                }
            } catch (e: Exception) {
                publish = false
            }
        } else {
            publish = false
        }

        if (publish) {
            create<MavenPublication>("mavenJava") {
                from(components["java"])

                artifact(javadocJar)

                pom {
                    groupId = publishGroup
                    artifactId = rootProject.base.archivesName.get().lowercase()
                    version = publishVersion
                    withXml {
                        asNode().appendNode("properties").appendNode("hash", hash)
                    }
                }
            }
        }
    }
    repositories {

        if (publishingValid) {
            maven {
                url = uri(mavenUrl!!)

                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        } else {
            mavenLocal()
        }
    }
}

extra {
    val properties = Properties()
    properties.load(FileInputStream(file("gradle/publishing.properties")))
    properties.forEach { (a, b) ->
        project.extra[a as String] = b as String
    }
}

val modrinth_id: String by extra
val release_type: String by extra
val changelog_file: String by extra

val modrinth_version = makeModrinthVersion(mod_version)
val display_name = makeName(mod_version)
val changelog_text = getChangelog(file(changelog_file))

fun makeName(version: String): String {
    return "$version (${minecraft_version})"
}

fun makeModrinthVersion(version: String): String {
    return "$version-mc${minecraft_version}"
}

fun getChangelog(changelogFile: File): String {
    val text = Files.readString(changelogFile.toPath())
    val split = text.split("-----------------")
    if (split.size != 2)
        throw IllegalStateException("Malformed changelog")
    return split[1].trim()
}

fun getBranch(): String {
    val env = System.getenv()
    var branch = env["GITHUB_REF"]
    if (branch != null && branch != "") {
        return branch.substring(branch.lastIndexOf("/") + 1)
    }

    if (grgit == null) {
        return "unknown"
    }

    branch = grgit.branch.current().name
    return branch.substring(branch.lastIndexOf("/") + 1)
}

modrinth {
    token.set(System.getenv("MODRINTH_TOKEN"))
    projectId.set(modrinth_id)
    versionNumber.set(modrinth_version)
    versionName.set(display_name)
    versionType.set(release_type)
    changelog.set(changelog_text)
    uploadFile.set(file("build/libs/${tasks.remapJar.get().archiveBaseName.get()}-$version.jar"))
    gameVersions.set(listOf(minecraft_version))
    loaders.set(listOf("fabric"))
    dependencies {
        required.project("fabric-api")
        required.project("fabric-language-kotlin")
        required.project("frozenlib")
        optional.project("fabric-kotlin-extensions")
        optional.project("modmenu")
        optional.project("cloth-config")
    }
}

val github by tasks.register("github") {
    dependsOn(remapJar)
    dependsOn(sourcesJar)
    dependsOn(javadocJar)

    val env = System.getenv()
    val token = env["GITHUB_TOKEN"]
    val repoVar = env["GITHUB_REPOSITORY"]
    onlyIf {
        token != null && token != ""
    }

    doLast {
        val github = GitHub.connectUsingOAuth(token)
        val repository = github.getRepository(repoVar)

        val releaseBuilder = GHReleaseBuilder(repository, makeModrinthVersion(mod_version))
        releaseBuilder.name(makeName(mod_version))
        releaseBuilder.body(changelog_text)
        releaseBuilder.commitish(getBranch())
        releaseBuilder.prerelease(release_type != "release")

        val ghRelease = releaseBuilder.create()
        ghRelease.uploadAsset(tasks.remapJar.get().archiveFile.get().asFile, "application/java-archive")
        ghRelease.uploadAsset(tasks.remapSourcesJar.get().archiveFile.get().asFile, "application/java-archive")
        ghRelease.uploadAsset(javadocJar.outputs.files.singleFile, "application/java-archive")
    }
}

val publishMod by tasks.register("publishMod") {
    dependsOn(tasks.publish)
    dependsOn(github)
    dependsOn(tasks.modrinth)
}
