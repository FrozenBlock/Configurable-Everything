plugins {
    id("net.frozenblock.triangle.neoforge")
}

val mod_version: String by project
val minecraft_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val frozenlib_version: String by project

val neoforgeSnapshotMaven = findProperty("neoforge_snapshot_maven") as String?

base {
    archivesName.set(archives_base_name)
}

val release = findProperty("releaseType") == "stable"

version = getModVersion()
group = maven_group

tasks.jar {
    archiveClassifier.set("neoforge")
}

repositories {
    maven("https://maven.neoforged.net/releases") { name = "NeoForged" }
    if (!neoforgeSnapshotMaven.isNullOrBlank()) {
        maven(neoforgeSnapshotMaven) { name = "NeoForge Snapshots" }
    }
    flatDir {
        dirs("libs")
    }
}

neoforge {
    dependOn(project(":ce-common"))
    accessWidener(project(":ce-common"))
}

neoForge {
    accessTransformers {} // Required for transitive AW to apply!
}

tasks {
    processResources {
        val properties = mapOf("mod_version" to getModVersion())
        inputs.properties(properties)
        filesMatching("META-INF/neoforge.mods.toml") {
            expand(properties)
        }
    }

    withType(JavaCompile::class) {
        options.encoding = "UTF-8"
        options.release = 25
        options.isFork = true
        options.isIncremental = true
    }
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
val loaderVariants = setOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements")
configurations.all {
    if (name in loaderVariants) {
        attributes {
            attribute(loaderAttribute, "neoforge")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "neoforge")
            }
        }
    }
}

dependencies {
    api("net.frozenblock:frozenlib-neoforge:${frozenlib_version}")?.let {
        accessTransformers(it)
        interfaceInjectionData(it)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

fun getModVersion(): String {
    var version = "$mod_version-mc$minecraft_version"

    if (!release)
        version += "-unstable"

    return version
}

val changelogText = run {
    val split = rootProject.file("CHANGELOG.md").readText().split("-----------------")
    check(split.size == 2) { "Malformed changelog" }
    split[1].trim()
}

upload {
    maven {
        name.set("configurableeverything-neoforge")
    }

    forEach {
        changelog.set(changelogText)
    }

    curseforge {
        dependencies {
            required("frozenlib")
        }
    }

    modrinth {
        dependencies {
            required("frozenlib")
        }
    }
}
