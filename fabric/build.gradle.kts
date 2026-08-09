plugins {
    id("net.frozenblock.triangle.fabric")
    id("com.gradleup.shadow")
}

withKotlin()

val mod_id: String by project
val mod_version: String by project
val minecraft_version: String by project
val maven_group: String by project
val archives_base_name: String by project

val fabric_loader_version: String by project
val fabric_api_version: String by project
val fabric_kotlin_version: String by project
val frozenlib_version: String by project

val cloth_config_version: String by project
val modmenu_version: String by project

val sodium_version: String by project

base {
    archivesName.set(archives_base_name)
}

val release = findProperty("releaseType") == "stable"

version = getModVersion()
group = maven_group

tasks.jar {
    archiveClassifier.set("fabric")
}

fabric {
    dependOn(project(":ce-common"))
    accessWidener(project(":ce-common"))
    dataGen {
        owner = project(":ce-common")
        splitSourceSet("datagen")
    }
}

loom {
    runtimeOnlyLog4j.set(true)

    enableTransitiveAccessWideners = true
    interfaceInjection {
        enableDependencyInterfaceInjection = true
    }
}

repositories {
    flatDir {
        dirs("libs")
    }
}

val loaderAttribute = Attribute.of("io.github.mcgradleconventions.loader", String::class.java)
val loaderVariants = setOf("apiElements", "runtimeElements", "sourcesElements", "javadocElements", "includeInternal", "modCompileClasspath")
configurations.all {
    if (name in loaderVariants) {
        attributes {
            attribute(loaderAttribute, "fabric")
        }
    }
}
sourceSets.configureEach {
    listOf(compileClasspathConfigurationName, runtimeClasspathConfigurationName).forEach { variant ->
        configurations.named(variant) {
            attributes {
                attribute(loaderAttribute, "fabric")
            }
        }
    }
}

val includeApi: Configuration by configurations.creating
val includeImplementation: Configuration by configurations.creating
val shadowInclude: Configuration by configurations.creating

configurations {
    include {
        extendsFrom(includeImplementation)
        extendsFrom(includeApi)
    }
    implementation {
        extendsFrom(includeImplementation)
    }
    api {
        extendsFrom(includeApi)
    }
}

dependencies {
    implementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")

    // Fabric Language Kotlin. Required for Kotlin support.
    implementation("net.fabricmc:fabric-language-kotlin:$fabric_kotlin_version")

    // get deps manually because FKE cant give them to compile classpath without an error
    api(kotlin("scripting-common"))
    api(kotlin("scripting-jvm"))
    api(kotlin("scripting-jsr223"))
    api(kotlin("scripting-jvm-host"))
    api(kotlin("scripting-compiler-embeddable"))
    api(kotlin("scripting-dependencies"))
    api(kotlin("scripting-dependencies-maven"))

    // FrozenLib
    api("net.frozenblock:frozenlib-fabric:${frozenlib_version}")

    // Cloth Config
    api("me.shedaniel.cloth:cloth-config-fabric:${cloth_config_version}") {
        exclude(group = "net.fabricmc.fabric-api")
        exclude(group = "com.terraformersmc")
    }

    // Mod Menu
    implementation("com.terraformersmc:modmenu:${modmenu_version}")

    // Sodium
    //compileOnly("maven.modrinth:sodium:$sodium_version")
}

tasks {
    processResources {
        val properties = HashMap<String, Any>()
        properties["mod_id"] = mod_id
        properties["version"] = version
        properties["minecraft_version"] = "~26.2-" //minecraft_version
        properties["fabric_kotlin_version"] = ">=$fabric_kotlin_version"

        properties.forEach { (a, b) -> inputs.property(a, b) }

        // Only fabric.mod.json actually needs expansion.
        filesMatching("fabric.mod.json") {
            expand(properties)
        }
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {
        archiveClassifier = ""
        configurations = listOf(shadowInclude)
        enableAutoRelocation = true
        relocationPrefix = "net.frozenblock.configurableeverything.shadow"
    }

    withType(Test::class) {
        maxParallelForks = Runtime.getRuntime().availableProcessors().div(2)
    }
}

val sourcesJar: Jar by tasks
val javadocJar: Jar by tasks

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

artifacts {
    archives(sourcesJar)
    archives(javadocJar)
}

fun getModVersion(): String {
    var version = "$mod_version-mc$minecraft_version"

    if (!release) {
        version += "-unstable"
    }

    return version
}

val changelogText = run {
    val split = rootProject.file("CHANGELOG.md").readText().split("-----------------")
    check(split.size == 2) { "Malformed changelog" }
    split[1].trim()
}

upload {
    maven {
        name.set("configurableeverything-fabric")
    }

    forEach {
        changelog = changelogText
    }

    curseforge {
        dependencies {
            required("fabric-api")
            required("frozenlib")
            required("fabric-language-kotlin")
            optional("fabric-kotlin-extensions")
        }
    }

    modrinth {
        dependencies {
            required("fabric-api")
            required("frozenlib")
            required("fabric-language-kotlin")
            optional("fabric-kotlin-extensions")
        }
    }
}
